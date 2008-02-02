========================================================
Project name : IRPhoto
Version : 1.0
Authors : Tom Miette & Sebastien Mouret
--------------------------------------------------------
README.txt
Created January 2, 2008 by Tom Miette & Sebastien Mouret
========================================================

What is IRPhoto ?
-----------------

IRPhoto is a small application to manage a collection of photos albums.
This software shows miniatures pictures of each photos and enables to 
locate these photos on a world map.

Project directories
-------------------

The project root directory is divided into six main directories.

	1. "bin" which contains the executable jar archive.
	2. "classes" which contains java binaries files (class files).
	3. "docs" which contains the project documentation.
	4. "docs/api" which contains the java documentation (javadoc format).
	5. "lib" which contains java external libraries.
	6. "src" which contains java source files (java files).

Building jar archive
--------------------

The following commands will build the executable jar archive in the
"bin" directory : 
	
	$ ant
	OR
	$ ant jar

Quick start
-----------

To run the program, you can use the build file with the "run" task as
following :

	$ ant run

Or you can simply use the java command to launch the jar archive :

	$ java - jar ${user_directory}/bin/IRPhoto.jar

Others ant tasks
----------------

The build file has others ant tasks 

	1. To compile java source files in the "classes" directory :
	
		$ ant compile
		
	2. To generate java documentation in the "docs/api" directory :
	
		$ ant javadoc

	3. To clean the project directory. This task removes class files, 
	documentation files and the jar archive :
	
		$ ant clean

Libraries
---------

The application used external libraries to run. These libraries are in the
"lib" directory and enable to perform graphical effects not included in
the default swing api.

	1. swingx to display the world map and to manage photo localisations.
	2. metadata-extractor to extract exif informations from photos files.

Saved files
-----------

IRPhoto saves its data.
When the application starts, it looking for a saved file in the current directory. 
This file is always named "albums.sav". When the application is closed, data are saved 
in new saved file which is put in the current directory as default.

To recover your saved data when you restart the application, you have to run the
jar archive from the same directory or to move your "albums.sav" file to the
current directory.

Known bugs
----------

The following section lists all known bugs of IRPhoto :

	1. 
	2.
