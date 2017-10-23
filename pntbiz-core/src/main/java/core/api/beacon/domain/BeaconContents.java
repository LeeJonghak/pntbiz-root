package core.api.beacon.domain;

import core.api.contents.domain.Contents;
import core.api.geofencing.domain.CodeAction;

import java.util.ArrayList;

public class BeaconContents extends Beacon {

    private ArrayList<Contents> contents = new ArrayList<Contents>();
    private ArrayList<CodeAction> codeActions = new ArrayList<CodeAction>();
	
    public ArrayList<Contents> getContents() {
        return contents;
    }

    public void setContents(ArrayList<Contents> contents) {
        this.contents = contents;
    }

    public ArrayList<CodeAction> getCodeActions() {
        return codeActions;
    }

    public void setCodeActions(ArrayList<CodeAction> codeActions) {
        this.codeActions = codeActions;
    }
}
