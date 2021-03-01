#include <wiringPi.h>
#include <SimpleAmqpClient/SimpleAmqpClient.h>
#include <iostream>
#include <json.hpp>
#include <chrono>
#include <thread>
#include <stdio.h>
#include <string.h>
#include <errno.h>
#include <stdlib.h>
#include <sstream>
#include <map>

using namespace AmqpClient;
using namespace std;
using nlohmann::json;

Channel::ptr_t connection;
bool state  = true;

map<int, int> mapping = {
  {0, 1},
  {1, 3}
};

bool isOpened[] = {false, false};

void publish(string message){
    connection->BasicPublish("", "locker-status", BasicMessage::Create(message), false, false);
    cout << "Message sent: " << message << endl;
}

void publish(string queue, string message, string corId) {
    BasicMessage::ptr_t msg = BasicMessage::Create(message);
    msg->CorrelationId(corId);
    connection->BasicPublish("", queue, msg, false, false);
}


void sendLockerState(int lockerId, int st){
	stringstream ss;
	ss << lockerId;
	json state;
	state["type"] = st ? "isLocked" : "isUnlocked";
	state["data"] = ss.str();
	publish(state.dump());
}

void checkAndSendLockerState(int lockerId){
  int n = digitalRead(3);
  if(n) {
      sendLockerState(lockerId, n);
  }
}

int last = -1;
void myInterrupt(){
    int n = digitalRead(3);
    this_thread::sleep_for(std::chrono::milliseconds(100));
    if(n == digitalRead(3)){
      if(last != digitalRead(3)){
        last = n;
	if(!isOpened[0]){
            sendLockerState(0, last);
	}
      }
    }
}

int main(){
    wiringPiSetup();
    pinMode(0, OUTPUT);
    pinMode(2, OUTPUT);
    pinMode(3, INPUT);
    digitalWrite (0, LOW); 
    
    // set Pin 17/0 generate an interrupt on high-to-low transitions
    // and attach myInterrupt() to the interrupt
    if ( wiringPiISR (3, INT_EDGE_BOTH, &myInterrupt) < 0 ) {
        fprintf (stderr, "Unable to setup ISR: %s\n", strerror (errno));
        return 1;
    }
    
    connection = Channel::Create("5.160.146.108", 5672, "admin", "HameShakha");
    Channel::ptr_t con2 = Channel::Create("5.160.146.108", 5672, "admin", "HameShakha");


    connection->DeclareQueue("locker-job", false, true, false, false);
    connection->DeclareQueue("locker-status", false, true, false, false);
    
    string consumerTag = con2->BasicConsume("locker-job", "", true, true, false);
    while(true){
       Envelope::ptr_t env = con2->BasicConsumeMessage(consumerTag);
       cout << "Message received: " << env->Message()->Body() << endl;
       json res = json::parse(env->Message()->Body());
       string type = res["type"].get<string>();
       int id = atoi(res["data"].get<string>().c_str());
       cout << type << " " << id << endl;
       state = !state;
       if(type == "open" && mapping[id] != 0){
	  isOpened[id] = true;
          digitalWrite (mapping[id] - 1, HIGH); 
          publish(env->Message()->ReplyTo(), "true", env->Message()->CorrelationId());
          this_thread::sleep_for(std::chrono::milliseconds(2000));
          digitalWrite (mapping[id] - 1, LOW);
	  isOpened[id] = false; 
          checkAndSendLockerState(0);
       }
    }    
    
    return 0;
}
