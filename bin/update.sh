#!/bin/bash

echo "Downloading newest EAICS from server"
wget --no-check-certificate 'https://docs.google.com/uc?export=download&id=1Jz-ifRSDt2IiIyk3ErSMKM5-SYkG-1-K' -O /home/pi/EAICS/EAICS.jar

echo "Completed download, copying files"

mv /home/pi/EAICS/dist/EAICS.jar /home/pi/EAICS/old/EAICS_prev.jar
cp /home/pi/EAICS/EAICS.jar /home/pi/EAICS/dist/EAICS.jar

echo "Update complete!"





