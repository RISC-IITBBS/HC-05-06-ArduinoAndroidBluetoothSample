// HC05 Rx to Arduino Tx
// HC05 Tx to Arduino Rx
char data = 0;
String s = "";

void setup()
{
  Serial.begin(9600);
}

void loop()
{
   if(Serial.available() > 0) {
    data = Serial.read();
    s = s + data;
    if(data=='\n') {
      Serial.print(s);
      s = "";
    }
   } 
}
