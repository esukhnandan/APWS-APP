



/**
 * Copyright (c) 2023 mobizt
 *
 */


#include <Arduino.h>
#if defined(ESP32) || defined(ARDUINO_RASPBERRY_PI_PICO_W)
#include <WiFi.h>
#elif defined(ESP8266)
#include <ESP8266WiFi.h>
#elif __has_include(<WiFiNINA.h>)
#include <WiFiNINA.h>
#elif __has_include(<WiFi101.h>)
#include <WiFi101.h>
#elif __has_include(<WiFiS3.h>)
#include <WiFiS3.h>
#endif

#include <Firebase_ESP_Client.h>

// Provide the RTDB payload printing info and other helper functions.
#include <addons/RTDBHelper.h>

String path = "/plantdata/sensorReading";


/* 1. Define the WiFi credentials */
#define WIFI_SSID "WIFI_SSID"
#define WIFI_PASSWORD "PASSWORD"

/* 2. Define the RTDB URL */
#define DATABASE_URL "awps-8ae0b-default-rtdb.firebaseio.com" //<databaseName>.firebaseio.com or <databaseName>.<region>.firebasedatabase.app

/* 3. Define the Firebase Data object */
FirebaseData fbdo;

/* 4, Define the FirebaseAuth data for authentication data */
FirebaseAuth auth;

/* Define the FirebaseConfig data for config data */
FirebaseConfig config;

unsigned long dataMillis = 0;
int count1 = 0;
int count2 = 0;

#if defined(ARDUINO_RASPBERRY_PI_PICO_W)
WiFiMulti multi;
#endif

void setup()
{

    Serial.begin(115200);
    pinMode(A0, INPUT);
    pinMode(D6, OUTPUT);
#if defined(ARDUINO_RASPBERRY_PI_PICO_W)
    multi.addAP(WIFI_SSID, WIFI_PASSWORD);
    multi.run();
#else
    WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
#endif

    Serial.print("Connecting to Wi-Fi");
    unsigned long ms = millis();
    while (WiFi.status() != WL_CONNECTED)
    {
        Serial.print(".");
        delay(300);
#if defined(ARDUINO_RASPBERRY_PI_PICO_W)
        if (millis() - ms > 10000)
            break;
#endif
    }
    Serial.println();
    Serial.print("Connected with IP: ");
    Serial.println(WiFi.localIP());
    Serial.println();

    Serial.printf("Firebase Client v%s\n\n", FIREBASE_CLIENT_VERSION);

    

    /* Assign the database URL(required) */
    config.database_url = DATABASE_URL;

    config.signer.test_mode = true;

    

    // The WiFi credentials are required for Pico W
    // due to it does not have reconnect feature.
#if defined(ARDUINO_RASPBERRY_PI_PICO_W)
    config.wifi.clearAP();
    config.wifi.addAP(WIFI_SSID, WIFI_PASSWORD);
#endif

    
    Firebase.reconnectNetwork(true);

   
    fbdo.setBSSLBufferSize(4096 /* Rx buffer size in bytes from 512 - 16384 */, 1024 /* Tx buffer size in bytes from 512 - 16384 */);

    /* Initialize the library with the Firebase authen and config */
    Firebase.begin(&config, &auth);

    
}

void loop()
{
        float value = 0.01f;
        do {
        value = 0.01f;
        int reading = analogRead(A0);
        float newCount = (reading - 1050) /1500.0;
        value = ((value / newCount) * 420.0);
        } while (value < 0 || value > 100);
        String first = path + String(count1++);
        int soilValue = value;
        if (value < 10) {
          digitalWrite(D6, HIGH);
          delay(2000);
          digitalWrite(D6, LOW);
        }
        Serial.printf("Set int... %s\n", Firebase.RTDB.setInt(&fbdo, first, soilValue) ? "ok" : fbdo.errorReason().c_str());
        delay(2000);
}