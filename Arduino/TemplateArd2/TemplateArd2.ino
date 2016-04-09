///////////////// Buzzer
#define  c     3830    // 261 Hz
#define  d     3400    // 294 Hz
#define  e     3038    // 329 Hz
#define  f     2864    // 349 Hz
#define  g     2550    // 392 Hz
#define  a     2272    // 440 Hz
#define  b     2028    // 493 Hz
#define  C     1912    // 523 Hz
// Define a special note, 'R', to represent a rest
#define  R     0

//Αλλαγή των 2 από κάτω μεταβλητών ανάλογα την περίπτωση μας
const int SUMOFUNITS = 5 ;
String controllerName = "Ard2" ;
//Δεν πειράζουμε το κώδικα του προτύπου : Begin
String deviceName = "DRC" ;
class Unit
{    
public :
    static const String VERSION ;
    String name,tag,limit ;
    double value,max,min,diff ;
    int type,mode,pin1,pin2 ;
    bool isAnalog ;
    void setAll(String na,String ta,int ty,int mo,String va,double ma,double mi,String li,int pi1,int pi2,bool isAnal,float di) ;
    
    bool setValue(String unitValue) ;    
    bool setValue(double unitValue) ; 
    bool setMode(String unitMode);    
    String getValue();           
    String getMode();
    String getInfo(); 
    
} units[SUMOFUNITS] ;
const String Unit::VERSION  = "PMG:V1" ;
bool sending = true ;
int timer = 0 ;
//Δεν πειράζουμε το κώδικα του προτύπου : End

void setup()
{
  //Δεν πειράζουμε το κώδικα του προτύπου : Begin
  Serial.begin(9600);
  //Δεν πειράζουμε το κώδικα του προτύπου : End
  units[0].setAll("Mikrofono","",6,0,"0",300,0,"NL",A1,-1,true,0.0);  //Name,tag,type,mode,value,Max,Min,limit,pin1(output),pin2(input),isAnalog,diff            
  units[1].setAll("Fotias","",8,0,"0",255,0,"NL",A2,-1,true,0.0);  
  units[2].setAll("Mousiki","",1,1,"0",255,0,"128SW",2,-1,true,0.0);  
  units[3].setAll("Kinisis","",4,0,"0",255,0,"0.5SW",8,10,false,0.0); 
  units[4].setAll("Fotistiko","",2,1,"255",255,0,"128SW",9,-1,false,0.0);         
  pinMode (A1, INPUT);
  pinMode (A2, INPUT);      
  pinMode (8, INPUT);
  pinMode (2, INPUT);        
  pinMode(9,OUTPUT);
  pinMode(10,OUTPUT); 
}


void loop()
{
  //Δεν πειράζουμε το κώδικα του προτύπου : Begin
    timer++ ;
    if (Serial.available() > 0) {  
        readBytesFromUSB() ;
        timer=0 ; 
    }
    if (timer==5000)
        timer=0 ; 
    
    if ((sending) && (timer==0))
    {           
        int sumOfParam = 0 ;
        String parameters = "" ;
  //Δεν πειράζουμε το κώδικα του προτύπου : End
        delay(3) ;   

        int voice = getVoiceFromMicrophone(units[0].pin1) ;      
        if ((units[0].value-units[0].diff) > voice || (units[0].value+units[0].diff) < voice)
        {
            sumOfParam += 1 ;
            units[0].value = voice ;
            parameters += units[0].getValue();
        }     
        delay(3) ; 
        int fire = getFireFromFlame(units[1].pin1) ; 
        
        if ((units[1].value-units[1].diff) > fire || (units[1].value+units[1].diff) < fire)
        {
            sumOfParam += 1 ;
            units[1].value = fire ;
            parameters += units[1].getValue();
        }            
        delay(3) ;       
        if (units[2].value==255)
        {
            playSong(units[2].pin1);
            
            sumOfParam += 1 ;
            units[2].value=0 ;
            parameters += units[2].getValue();  
        }
        delay(3) ;
        int motion = getMotionState(units[3].pin1,units[3].pin2) ; 
        if (motion==HIGH && units[3].value != HIGH)
        {                      
            sumOfParam += 1 ;
            units[3].value=HIGH ;
            parameters += units[3].getValue();  
        }
        else if (motion==LOW && units[3].value != LOW)
        {
            sumOfParam += 1 ;
            units[3].value=LOW ;
            parameters += units[3].getValue();   
        }
    //Δεν πειράζουμε το κώδικα του προτύπου : Begin    
        if (sumOfParam>0)
        {          
            String post = "#"+deviceName+"$NewValues:"+  String(sumOfParam)+"*" ;
            Serial.println(post+parameters+";");          
        }        
    //Δεν πειράζουμε το κώδικα του προτύπου : End
    }   
}
//Συναρτήσεις ανάλογα τις μονάδες μας :
const int sampleWindow = 200;
int getVoiceFromMicrophone(int pin)
{
  unsigned long startMillis= millis();  // Start of sample window
   unsigned int peakToPeak = 0;   // peak-to-peak level
 
   unsigned int signalMax = 0;
   unsigned int signalMin = 1024;
   int sample ;
   // collect data for 200 mS
   while (millis() - startMillis < sampleWindow)
   {
      sample = analogRead(pin);         
      if (sample < 1024)  // toss out spurious readings
      {
         if (sample > signalMax)
         {
            signalMax = sample;  // save just the max levels
         }
         else if (sample < signalMin)
         {
            signalMin = sample;  // save just the min levels
         }
      }
   }
   peakToPeak = signalMax - signalMin;  // max - min = peak-peak amplitude
    
    return peakToPeak ;  
}

int getFireFromFlame(int pin)
{
    int sensorValue = analogRead(pin);            
    
    return map(sensorValue, 0, 1023,255, 0);  
}
   
///////////buzzer
int melody[] = {  C,  b,  g,  C,  b,   e,  R,  C,  c,  g, a, C };
int beats[]  = { 16, 16, 16,  8,  8,  16, 32, 16, 16, 16, 8, 8 };
int MAX_COUNT = sizeof(melody) / 2; // Melody length, for looping.

// Set overall tempo
long tempo = 10000;
// Set length of pause between notes
int pause = 500;
// Loop variable to increase Rest length
int rest_count = 100; //<-BLETCHEROUS HACK; See NOTES

// Initialize core variables
int tone_ = 0;
int beat = 0;
long duration  = 0;

// PLAY TONE  ==============================================
// Pulse the speaker to play a tone for a particular duration
void playTone(int pin) {
    long elapsed_time = 0;
    if (tone_ > 0) { // if this isn't a Rest beat, while the tone has
        //  played less long than 'duration', pulse speaker HIGH and LOW
        while (elapsed_time < duration) {
            
            digitalWrite(pin,HIGH);
            delayMicroseconds(tone_ / 2);
            
            // DOWN
            digitalWrite(pin, LOW);
            delayMicroseconds(tone_ / 2);
            
            // Keep track of how long we pulsed
            elapsed_time += (tone_);
        }
    }
    else { // Rest beat; loop times delay
        for (int j = 0; j < rest_count; j++) { // See NOTE on rest_count
            delayMicroseconds(duration);  
        }                                
    }                                
}
void playSong(int pin)
{
    
    for (int i=0; i<MAX_COUNT; i++) {
        tone_ = melody[i];
        beat = beats[i];
        
        duration = beat * tempo; // Set up timing
        
        playTone(pin);
        // A pause between notes...
        delayMicroseconds(pause);
    }
}

/////////////Fotokytaro
long timeFotokytaro = 0 ;
int getMotionState(int pinIn,int pinOut)
{    
    if(digitalRead(pinIn) == HIGH){
        digitalWrite(pinOut, HIGH);   //the led visualizes the sensors output pin state
        timeFotokytaro = 0 ;    
        return HIGH ;  
    }    
    if(digitalRead(pinIn) == LOW){       
        if (timeFotokytaro<=1000)
            timeFotokytaro++ ;
        if (timeFotokytaro=1000)
        {        
            digitalWrite(pinOut, LOW);  
            return LOW ; 
        }        
    }
    
    return HIGH ;
    
}
//Τέλος συναρτήσεων των μονάδων μας!

//Δεν πειράζουμε το κώδικα του προτύπου : Begin
void readBytesFromUSB()
{
    int inByte = Serial.read();   
    if (inByte=='M' || inByte=='m')
    {
        String unitName = Serial.readString() ;
        int pos = unitName.indexOf( ":");
        String unitMode = unitName.substring(pos+1,unitName.length()) ;
        unitName = unitName.substring(0,pos) ;
        Unit* un = findUnit(unitName);
        if (un!=NULL)
        {
            if (un->setMode(unitMode)) ;/*
               {
                  String post = "#DemoRealCon@RealCon$ChangeModes:"+  String(1)+"*" ;
                  Serial.print(post);
                  Serial.print(un->getMode());
                  Serial.println(";"); 
               }             */
        }
    }
    else if (inByte=='C' || inByte=='c')
    {
        String unitName = Serial.readString() ;
        int pos = unitName.indexOf( ":");
        String unitValue = unitName.substring(pos+1,unitName.length()) ;
        unitName = unitName.substring(0,pos) ;
        Unit* un = findUnit(unitName);
        
        if (un!=NULL)
        {
            if (un->setValue(unitValue)) 
            {
                String post = "#"+deviceName+"$NewValues:"+  String(1)+"*" ;
                Serial.print(post);
                Serial.print(un->getValue());
                Serial.println(";");  
            }            
        }
    }
    else if (inByte=='B' || inByte=='b')
    {
        //Serial.println("Started!");
        sending = true ;
    }
    else if (inByte=='E' || inByte=='e')
    {
        Serial.println("Stopped!");
        sending = false ;
    }
    else if (inByte=='V' || inByte=='v')
    {
        Serial.println(Unit::VERSION); //PMG version
    }
    else if (inByte=='D' || inByte=='d')
    {        
        deviceName = Serial.readString() ;
        Serial.println(deviceName); 
    }
    else if (inByte=='I' || inByte=='i')
    {
        String post = "#"+deviceName+"$NewController:"+  String(SUMOFUNITS+1)+"*(" + controllerName + ")" ;  
        for (int i = 0 ;i<SUMOFUNITS;i++)
        {
            post += units[i].getInfo() ;
        }
        post += ";" ;
        Serial.println(post) ;
    }
    else if (inByte=='N' || inByte=='n')
    {
        controllerName = Serial.readString() ;        
        Serial.println("ok") ;    
    }
    else
        Serial.println(inByte);
};

void Unit::setAll(String na,String ta,int ty,int mo,String va,double ma,double mi,String li,int pi1,int pi2,bool isAnal,float di)
{
    name = na ;
    tag = ta ;
    pin1 = pi1 ;
    pin2 = pi2 ;
    type = ty ;
    isAnalog = isAnal ;
    mode =mo ;
    setValue(va);
    max = ma ;
    min = mi ;
    limit = li ;
    type = ty ;               
    diff = di;
}
String Unit::getValue()
{
    return "(" + controllerName + "|" +name +"|" + value + ")" ;
}
String Unit::getInfo()
{
    return "(" + name +"|" + type + "|" + mode + "|" + tag +"|" + value + "|" + max + "|" + min +"|" + limit + ")" ;
}
bool Unit::setValue(String unitValue)
{/*
  if ((mode!=0)&&(mode!=3))
   { */            
    value = atof(unitValue.c_str());
    if (isAnalog)
      analogWrite(pin1,value) ;
    else
      {
        if (value>128)
          digitalWrite(pin1,HIGH) ;
        else
          digitalWrite(pin1,LOW) ;
      }            
    /*      
   }
  else
   return false ;        */
 return true ;
}
bool Unit::setValue(double unitValue)
{/*
  if ((mode!=0)&&(mode!=3))
   {*/
    value = unitValue;
    if (isAnalog)
      analogWrite(pin1,value) ;
    else
      {
        if (value>128)
          digitalWrite(pin1,HIGH) ;
        else
          digitalWrite(pin1,LOW) ;
      }            
    /*      
    }
   else
     return false ;        */
   return true ;
}
bool Unit::setMode(String unitMode)
{
    int tmp = atoi(unitMode.c_str());
    if (tmp>=0 && tmp<=4)
    {
        mode = tmp ;
        return true ;
    }
    return false ;
}
String Unit::getMode()
{
    return "(" + controllerName + "|" +name +"|" + mode + ")" ;      
}
Unit* findUnit(String unitName)
{
    for (int i = 0;i<SUMOFUNITS;i++)
        if (units[i].name.equals(unitName))
            return &units[i] ;
    return (Unit*)NULL  ;
}
//Δεν πειράζουμε το κώδικα του προτύπου : End
/*
 * "No Category Dimming"
 ,"No  Category Switch"
 ,"Lamp"
 ,"Brightness"
 ,"Motion"
 ,"Distance"
 ,"Sound"
 ,"Vibration"
 ,"Smoke"
 ,"Temperature"
 ,"Humidity"
 ,"switch"
 */
