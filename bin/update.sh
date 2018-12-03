#!/bin/bash

echo "Downloading newest EAICS from server"
wget --no-check-certificate 'https://docs.google.com/uc?export=download&id=117MceC8bWqg_kwhnp8djZsVv2rFcvcWj' -O /home/pi/EAICS/download/EAICS.jar

echo "Completed download, copying files"

sudo mv /home/pi/EAICS/dist/EAICS.jar /home/pi/EAICS/old/EAICS_prev.jar
sudo cp /home/pi/EAICS/download/EAICS.jar /home/pi/EAICS/dist/EAICS.jar

echo "Update complete!"





