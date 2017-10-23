<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<p>
The page shows information about all registered beacons.
</p>

<hr />

<h2>[Beacon List] Page</h2>
<p>
The list contains all beacons registered by the owner. By clicking UUID or Beacon Name, 
you can access Modify Beacon page where you can see details of the settings 
for each beacon as well as changing setting of beacons.
</p>

<p>
By clicking column title, the list is sorted by clicked column. (Toggling â Ascending/Descending)
</p>
<p>
Description of each column in the beacon list is as below.
</p>

<ol>
	<li>No<br />-	Automatically generated unique ID (only internally used)</li>
	<li>UUID<br />-	Universally Unique Identifier. In this table, Beacon Major Version and Minor Version are added to UUID with underscore. (i.e. UUID_Major Version_Minor Version)</li>
	<li>Beacon Name<br />-	Name of beacon. Owner can make any preferred name. (Does not have to be unique)</li>
	<li>Battery Level<br />-	It displays current battery level of the beacon. The range of batter level is from 0 to 100.</li>
	<li>TxPower<br />-	TxPower(Transmission Power) tells beaconâs signal strength at 1 meter. Note that this field is currently used as reserved column.</li>
	<li>Floor<br />-	Floor the beacon is installed. Any preferred name can be used. (* Floor information is managed at Map->Floor Management menu)</li>
	<li>Registered Date<br />-	  The date & time beacon is registered. (Automatically created when register)</li>
</ol>
</p>

<h3>Register Button</h3>
<p>
Go to Register Beacon page.
</p>

<h3>Search Option</h3>
<p>
You can search beacons by category listed.
</p>

<ol>
	<li>Beacon Name<br />-	Search result contains only beacons with the beacon name starting with the letter(s) you enter.</li>
	<li>Floor<br />-	Search result contains only beacons with floor name starting with the letter(s) you enter.</li>
	<li>Major Version<br />-	Search result contains only beacons with major version starting with the letter(s) you enter.</li>
	<li>Minor Version<br />-	Search result contains only beacons with minor version starting with the letter(s) you enter.</li>
</ol>
<p>
You can reset search by clicking search button with empty search letter.
</p>