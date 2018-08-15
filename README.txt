Author:
	Tanner Woody
Date:
	20180815
Support:
	Android - YES
		MinSDK:
			???
	IOS - NOT YET IMPLEMENTED
	
Technologies:
	Android Studio
	SQL
	XML
	Java

Purpose:
	This is desinged to be a task/TODO/checklist app.
	The user should name their checklist in the homescreen.
	To create a new checklsit, click the Floating Action Button (FAB).
	The named checklist will be a button linking the user to the empty checklist.
	Within the checklist, similarly to the homescreen, use the FAB to add tasks to the list.
	The user can then mark what items have been completed, and what items will need to be commpleted.
	Completed items will go into the green zone, where they are marked complete.
	Added items will always be appended to the end of the list.

(Not yet implemented & TODO)
	A. The user should be able to `select` an item.
		1. Selecting an item will open a new activity, designed for editing.
		2. The user should be able to: 
			a. rearrange items
			b. mass delete items
			c. export selected items to another list
		3. SELECT ALL
	B. There needs to be CONFIRMATION WINDOWS ensuring the user wants to delete and export.
	C. The area for completed tasks should only be visible when at least one item is completed.
	D. Font, sizing, style, and colors need to be configured.
		1. TEST making the checkbox text link to the checkbox.
			a. Do not want user to accidentally click items;
		2. Go over coloring and fonts with design specialist;
	E. Link to Developer page for user input should be added;
		1. Developer page might be personal website;
		2. Developer page might link to Play Store;
	F. Export checklist to:
		1. Google notes
		2. Text Message
		3. SQL file download
		4. Excell file download
	G. An `about` for the app with functionality needs to created.
	H. Enable `reminders` to send notification to the user
		1. Inlist notifications to android wear
	I. Fix top app bar to have dynamic display AND MORE
		1. Name of checklist
		2. Number of completed tasks out of number of total tasks
		3. Back button
	J. Allow users to pick their own color and font schema;
	K. Fix database management
		1. db.close() best practice
		2. Test data consumption;
		3. Test storage consumption;
		4. Test max length of characters that are allowed

