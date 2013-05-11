#!/bin/bash
adb shell monkey -p se.kjellstrand.robot --monitor-native-crashes --ignore-security-exceptions --pct-majornav 30 --throttle 50 --ignore-timeouts $1 
