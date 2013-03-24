/*
  A5 ArduinoMorse
  Blinks LED in morse based on serial input.
*/

// Converts ASCII value to character table position
#define MORSIFY(a) \
       (a > 47 && a < 58)  ? a - 22 : \
       (a > 64 && a < 91)  ? a - 65 : \
       (a > 96 && a < 123) ? a - 97 : \
        36;

// Pin 13 has an LED connected on most Arduino boards.
// give it a name:
int led = 13;

// 36 characters  + 1 terminator
// 5 morse digits + 1 terminator
const char morse_str[37][6]   = {
  ".-",    "-...",  "-.-.",   // ABC
  "-..",   ".",     "..-.",   // DEF
  "--.",   "....",  "..",     // GHI
  ".---",  "-.-",   ".-..",   // JKL
  "--",    "-.",    "---",    // MNO
  ".--.",  "--.-",  ".-.",    // PQR
  "...",   "-",     "..-",    // STU
  "...-",  ".--",   "-..-",   // VWX
  "-.--",  "--..",            // YZ 
 
  "-----", ".----", "..---",   // 012
  "...--", "....-", ".....",   // 345
  "-....", "--...", "---..",   // 678
  "----.", "\0",               // 9, TERM 
};

// Allocate character buffer
char buf[1024];

// Forward Declaration
void send_morse(char c);

// the setup routine runs once when you press reset:
void setup() {                
  // initialize the digital pin as an output.
  pinMode(led, OUTPUT);
  
  // Set up the serial connection
  Serial.begin(9600);
  int i = 0;
  for (i=0;i<1024;++i) {
    buf[i] = '\0';
  }
}

void send_morse(char c) {
  int i = 0;
  int character = MORSIFY(c);

  for (i=0; morse_str[character][i]!='\0'; ++i) {
    switch(morse_str[character][i]) {
      case '.':
        digitalWrite(led, HIGH);   
        delay(100);
        digitalWrite(led, LOW);
        delay(300); 
        Serial.write(".");
        break;
      case '-':
        digitalWrite(led, HIGH);   
        delay(300);
        digitalWrite(led, LOW);
        delay(300);
        Serial.write("-");
        break;
      default:
        Serial.write("*default*");
        delay(400);
        break;
    }
  }
  Serial.write(" ");
  return;
}

void send_ccode(char c) {
  int i = 0;
  int character = MORSIFY(c);
  Serial.print(character);
  return;
}

// the loop routine runs over and over again forever:
void loop() {
  // Note the M$ Windoze carriage return
  int sz = 0;
  sz = Serial.readBytesUntil('\r\n', buf, 1024);
  
  // For each character in the buffer
  int i=0;
  for (i=0;i<sz;++i) {
    send_morse(buf[i]);
   // send_ccode(buf[i]);
  }
}
