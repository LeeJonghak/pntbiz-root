<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<h2>[Modify Beacon] page</h2>

<p>
The page is for updating setting of the beacon and deleting the beacon.
</p>

<h4>Modify Beacon</h4>

<p>
Description of each field of beacon setting is as below. You can enter each value and you can apply new settings by clicking Modify Button. You can also delete beacon by clicking Delete Button.
</p>

<ol>
	<li>UUID<br />-	Universally Unique Identifier for the beacon. Normally UUID is provided from manufacturer.</li>
	<li>Major Version<br />-	Major Version of beacon</li>
	<li>Minor Version<br />-	Minor Version of beacon</li>
	<li>Beacon Name<br />-	Name of beacon. Owner can make any name. It does not have to be unique.</li>
	<li>TxPower<br />-	This field is reserved field.</li>
	<li>Latitude/Longitude<br />-  Click Show Map button and then can click the location of the beacon in the map with mouse. Latitude and longitude values are automatically generated according to the location. </li>
	<li>Floor<br />-	Select the floor the beacon is installed. Note that you need to create floor(s) in advance through Map->Floor Management menu.</li>
	<li>Description<br />-	Any additional comments about the beacon</li>
</ol>

<h4>Delete Button</h4>

<p>
By clicking Delete Button, you can delete the beacon.
</p>

<h4>List Button</h4>

<p>
By clicking List Button, you can access Beacon Status Check page.
</p>