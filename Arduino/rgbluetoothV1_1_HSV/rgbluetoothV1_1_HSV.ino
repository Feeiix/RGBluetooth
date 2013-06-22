#include <Event.h>
#include <Timer.h>

const int redPin = 6;   
const int greenPin = 9;
const int bluePin = 10;
int red = 0;
int green = 0;
int blue = 0;
int fred = 0;
int fgreen = 0;
int fblue = 0;
int redold;
int greenold;
int blueold;
char firstChar;
Timer t;
int fTimer;
char fColor;
int fSpeed;
int fStep = 4;
boolean faded = false;
String mode;
int h;
int s;
int v;

void setup() { 
  Serial.begin(19200);
  pinMode(redPin, OUTPUT);
  pinMode(greenPin, OUTPUT);
  pinMode(bluePin, OUTPUT);
  shift(); //
}

void loop() {
  t.update();
}

void serialEvent() {

  while (Serial.available()) {

    firstChar = (char)Serial.read();

    switch (firstChar) {

    case 'C': // Key for a single color
      t.stop(fTimer);
      h = Serial.parseInt(); 
      s = Serial.parseInt(); 
      v = Serial.parseInt(); 

      //if (Serial.read() == '\n') {
        HSV_to_RGB(h,s,v);
      //}
      break;

    case 'P': // Key for the RGB-fading
      t.stop(fTimer);
      fSpeed = Serial.parseInt();
      fColor = 'R';
      fSpeed = constrain(fSpeed, 1, 100);
      fTimer = t.every(fSpeed, rgbfade);
      break;

    case 'F': // Key for pulsing the color
      t.stop(fTimer);
      fSpeed = Serial.parseInt();
      fSpeed = constrain(fSpeed, 1, 100);
      mode = "pulse";
      faded = false;
      redold = red;
      greenold = green;
      blueold = blue;
      fTimer = t.every(fSpeed, colorfade);  

    }    

  }


}

void colorfade(){

  if (mode == "pulse"){
    if (red == redold && green == greenold && blue == blueold){
      faded = false;
    }
    if (!faded){
      fred = 0;
      fgreen = 0;
      fblue = 0;
    }
    else {
      fred = redold;
      fgreen = greenold;
      fblue = blueold;
    }
  }

  if (red > fred){
    red = red--;
  }
  if (red < fred){
    red = red++;
  }
  if (green > fgreen){
    green = green--;
  }
  if (green < fgreen){
    green = green++;
  }
  if (blue > fblue){
    blue = blue--;
  }
  if (blue < fblue){
    blue = blue++;
  }
  if (red == fred && green == fgreen && blue == fblue){
    faded = true;
  }
  shift();
}

void rgbfade() {

  switch (fColor) {

  case 'R':
    green = green + 4;
    red = red - 4;
    shift();
    if (green == 255 && red == 0) {
      fColor = 'G';
    }
    break;

  case 'G':

    blue = blue + 4;
    green = green - 4;
    shift();
    if (green == 0 && blue == 255) {
      fColor = 'B';
    }
    break;

  case 'B':

    red = red + 4;
    blue = blue - 4;
    shift();
    if (blue == 0 && red == 255) {
      fColor = 'R';
    }
    break;
  }
}

void shift() {
  red = constrain(red, 0, 255);
  green = constrain(green, 0, 255);
  blue = constrain(blue, 0, 255);
  Serial.println((String)red + "," + (String)green + "," + (String)blue);
  analogWrite(redPin, red);
  analogWrite(greenPin, green);
  analogWrite(bluePin, blue);
}

long HSV_to_RGB(int h, int s, int v ) {
  /*
     modified from Alvy Ray Smith's site:
   http://www.alvyray.com/Papers/hsv2rgb.htm
   H is given on [0, 6]. S and V are given on [0, 1].
   RGB is returned as a 24-bit long #rrggbb
   */
  int i;
  int m, n, f;

  // not very elegant way of dealing with out of range: return black
  if ((s<0) || (s>1) || (v<0) || (v>1)) {
    return 0L;
  }

  if ((h < 0) || (h > 6)) {
    
  }
  i = h;
  f = h - i;
  if ( !(i&1) ) {
    f = 1 - f; // if i is even
  }
  m = v * (1 - s);
  n = v * (1 - s * f);
  switch (i) {
  case 6:
  case 0: // RETURN_RGB(v, n, m)
    red = long(v * 255 ) * 65536; green = long( n * 255 ) * 256; blue = long( m * 255);
    shift();
    break;
  case 1: // RETURN_RGB(n, v, m) 
    red = long(n * 255 ) * 65536; green = long( v * 255 ) * 256; blue = long( m * 255);
    shift();
    break;
  case 2:  // RETURN_RGB(m, v, n)
    red = long(m * 255 ) * 65536; green = long( v * 255 ) * 256; blue = long( n * 255);
    shift();
    break;
  case 3:  // RETURN_RGB(m, n, v)
    red = long(m * 255 ) * 65536; green = long( n * 255 ) * 256; blue = long( v * 255);
    shift();
    break;
  case 4:  // RETURN_RGB(n, m, v)
    red = long(n * 255 ) * 65536; green = long( m * 255 ) * 256; blue = long( v * 255);
    shift();
    break;
  case 5:  // RETURN_RGB(v, m, n)
    red = long(v * 255 ) * 65536; green = long( m * 255 ) * 256; blue = long( n * 255);
    shift();
    break;
  }
} 









