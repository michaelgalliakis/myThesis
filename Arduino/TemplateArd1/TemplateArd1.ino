#include "DHT.h"
#define DHTPIN 2  
#define DHTTYPE DHT11   // DHT 11

//Αλλαγή των 2 από κάτω μεταβλητών ανάλογα την περίπτωση μας
const int SUMOFUNITS = 8 ;
String controllerName = "Ard1" ;
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

DHT dht(DHTPIN, DHTTYPE);
bool firstTime = true; 

void setup()
{
    //Δεν πειράζουμε το κώδικα του προτύπου : Begin
    Serial.begin(9600);
    //Δεν πειράζουμε το κώδικα του προτύπου : End
    units[0].setAll("thermometro","",9,0,"0",100,-40,"NL",A0,-1,true,0.5); //Name,tag,type,mode,value,Max,Min,limit,pin1(output),pin2(input),isAnalog,diff            
    units[1].setAll("Sonar","",5,0,"0",3000,0,"NL",4,3,false,0.0);      
    units[2].setAll("thermApoYgra","",9,0,"0",100,-40,"NL",2,-1,false,0.5);  
    units[3].setAll("Ygrasia","",10,0,"0",100,-40,"NL",2,-1,false,0.0);          
    units[4].setAll("Fotoaistitiras","",3,0,"0",255,0,"NL",A3,-1,true,0.0);  
    units[5].setAll("ledFotoaistitira","",2,3,"255",255,0,"1TB",9,-1,true,0.0);      
    units[6].setAll("Diakoptis","",11,2,"0",2,0,"1SW",7,-1,true,0.0);  
    units[7].setAll("LampakiDiakopti","",2,0,"0",255,0,"128SW",12,-1,true,0.0);  
    pinMode (A0, INPUT);    
    pinMode (3, INPUT);
    pinMode (4, OUTPUT);
    pinMode (2, INPUT);    
    pinMode (A3, INPUT);
    pinMode (9, OUTPUT);
    pinMode (7, INPUT);
    pinMode (12, OUTPUT);
    //always 5V:
    pinMode (8, OUTPUT);
    digitalWrite(8,HIGH) ;
    
    dht.begin();
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
        float cel = getCelFromTemp(units[0].pin1) ;      
        if ((units[0].value-units[0].diff) > cel || (units[0].value+units[0].diff) < cel)
        {
            sumOfParam += 1 ;
            units[0].value = cel ;
            parameters += units[0].getValue();
        }
        delay(3) ;
        float cm = getCMFromSonar(units[1].pin1,units[1].pin2) ;      
        if ((units[1].value-units[1].diff) > cm || (units[1].value+units[1].diff) < cm)
        {
            sumOfParam += 1 ;
            units[1].value = cm ;
            parameters += units[1].getValue();
        }  
        delay(3) ;
        float cel2 = getCelFromHumidityTemp(units[2].pin1) ;      
        if ((units[2].value-units[2].diff) > cel2 || (units[2].value+units[2].diff) < cel2)
        {
            sumOfParam += 1 ;
            units[2].value = cel2 ;
            parameters += units[2].getValue();
        } 
        delay(3) ; 
        float hum = getHumFromHumidityTemp(units[3].pin1) ;      
        if ((units[3].value-units[3].diff) > hum || (units[3].value+units[3].diff) < hum)
        {
            sumOfParam += 1 ;
            units[3].value = hum ;
            parameters += units[3].getValue();
        } 
        delay(3) ;      
        double Brightness = getBrightnessFromPhotosensor(units[4].pin1) ;         
        if ((units[4].value-units[4].diff) > Brightness || (units[4].value+units[4].diff) < Brightness)
        {
            sumOfParam += 1 ;
            units[4].value = Brightness ;
            parameters += units[4].getValue();
        }    
        delay(3) ;
        if (units[5].mode==3 || units[5].mode==0 || units[5].mode==2)
        {
            
            int tmpValue = map(units[4].value, 8, 300,255, 0); 
            if (tmpValue<0) tmpValue = 0 ;
            if (tmpValue>255) tmpValue = 255 ;          
            if ((units[5].value-units[5].diff) > tmpValue || (units[5].value+units[5].diff) < tmpValue)
            {            
                sumOfParam += 1 ;
                units[5].setValue(tmpValue) ;
                parameters += units[5].getValue();  
            }            
        }
        delay(3) ;
        if (getButtonState(units[6].pin1)==HIGH)
          {
            if (firstTime)
              {
                sumOfParam += 1 ;
                units[6].value = (units[6].value==0)?2:0 ;
                parameters += units[6].getValue();
                
                sumOfParam += 1 ;
                units[7].setValue((units[6].value==2)?255:0) ;
                parameters += units[7].getValue();
                firstTime = false ;  
            }            
          }      
        else
          {
            firstTime = true ;
          }
        if (units[6].value==2 && units[7].value == 0)
          {
            sumOfParam += 1 ;
            units[7].setValue(255) ;
            parameters += units[7].getValue();
          }
        else if (units[6].value==0 && units[7].value == 255)
          {
            sumOfParam += 1 ;
            units[7].setValue(0) ;
            parameters += units[7].getValue();          
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
int getButtonState(int pin)
{
    return digitalRead(pin);
}
double getBrightnessFromPhotosensor(int pin)
{
    double sensorValue = analogRead (pin);
    
    return sensorValue; 
}

float getCelFromHumidityTemp(int pin)
{
    float t = dht.readTemperature();
    if (isnan(t)) {
        return -1 ;
    }
    return t ;
}
float getHumFromHumidityTemp(int pin)
{
    float h = dht.readHumidity();
    if (isnan(h)) {
        return -1 ;
    }
    return h ;
}

float getCelFromTemp(int pin)
{
    int val = analogRead(pin);
    float mv = ( val/1024.0)*5000;
    return mv/10;  
}

long getCMFromSonar(int trig,int echo)
{
    long duration;
        
    digitalWrite(trig, LOW);
    delayMicroseconds(2);
    digitalWrite(trig, HIGH);
    delayMicroseconds(5);
    digitalWrite(trig, LOW);
            
    duration = pulseIn(echo, HIGH);
    
    // convert the time into a distance
    int result = duration / 29 / 2 ;
    if (result>=1000)  
        return 0;
    else
        return result ;
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
