>Problem description:

Write a program that solves the most suitable (with most power) link station for a device at given
point [x,y].
Please make this project as complete as you think it should be to be maintainable in the long
term by more than one maintainer. Provide instructions how to run the solution or if applicable
how to access a deployed running version.
This problem can be solved in 2-dimensional space. Link stations have reach and power.
A link station’s power can be calculated:
power = (reach - device's distance from linkstation)^2
if distance > reach, power = 0
Program should output following line:
“Best link station for point x,y is x,y with power z”
or:
“No link station within reach for point x,y”

>Method of procedure:

- Pre-requisities:
1) Make sure your java version is Java SE 16:
java version "16.0.2" 2021-07-20
Java(TM) SE Runtime Environment (build 16.0.2+7-67)
Java HotSpot(TM) 64-Bit Server VM (build 16.0.2+7-67, mixed mode, sharing)

- Run application:
1) Unzip the file "LinkStation-NordCloudTest.zip";
2) Open the Windows Command line and change your current directory to the unzipped folder;
3) Run the command "java -jar FindBestLinkStation.jar <args>", where args can take 2 types of input:
	A) (X,Y) - represents the device position in the X,Y format;
	B) (X,Y,R) - represents the link station position X,Y and its reach value R format;

By default, there are already 3 link stations ((0,0,10); (20,20,5); (10,0,12)) added to the list. However, if the user wishes to test a device with extra link stations, he can use the (X,Y,R) argument.

- Example:

1) Multiple device positions:
C:\...\eclipse-workspace\LinkStation_NordCloudTest>java -jar FindBestLinkStation.jar (10,10) (100,100) (15,10) (18,18)

Best link station for point 10,10 is 10,0 with power 4
No link station within reach for point 100,100
No link station within reach for point 15,10
Best link station for point 18,18 is 20,20 with power 4

2) Multiple device positions with 2 extra link stations:
C:\...\eclipse-workspace\LinkStation_NordCloudTest>java -jar FindBestLinkStation.jar (10,10) (100,100) (15,10) (18,18) (90,90,20) (5,5,5)

Best link station for point 10,10 is 10,0 with power 4
Best link station for point 100,100 is 90,90 with power 34
No link station within reach for point 15,10
Best link station for point 18,18 is 20,20 with power 4
