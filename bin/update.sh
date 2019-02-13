#!/bin/bash

echo "Downloading newest EAICS from server"
wget --no-check-certificate 'http://eaics.x10host.com/EAICS.jar' -O /home/pi/EAICS/download/EAICS.jar

echo "Completed download, copying files"

mv /home/pi/EAICS/dist/EAICS.jar /home/pi/EAICS/old/EAICS_prev.jar
cp /home/pi/EAICS/download/EAICS.jar /home/pi/EAICS/dist/EAICS.jar

echo "Update complete!"


sleep 5





