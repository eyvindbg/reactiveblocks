package taxiproject.system.component;

import no.ntnu.item.arctis.runtime.Block;

public class Component extends Block {

	public java.lang.String g;


	/*
	//TODO:
	
	Ordne cancel order. Dette krever at ordren slettes og sier fra til de den skal (til dispatch, til assignet drosje, og bekreftelse p� slettet order
	tilbake til bruker som trykket cancel
	
	Ordne confirm order fra taxiclient-siden. Etter �n taxi har godtatt en ordre, skal ikke andre taxier lenger kunne confirme.
	
	Det er kun siste objekt som er aktivt for taxiclientene. Dersom UserMSID0 legger inn en order, 1, rett etter UserMSID1 har lagt inn order 0, 
	uten at noen har rukket � svare p� order 0, vil det ikke lenger g� an � aksessere ordren
	
	Det g�r an � f� opp taxier og fjerne dem fra kartet. Mangler � ta hensyn til dersom bruker trykker "offduty" dersom den allerede er "offduty", og
	svarende med "onduty". Da krasjer programmet
	
	N�r et taxiclient-objekt opprettes, m� det sendes til dispatch og legges til i listen over tilgjengelige taxier
	
	*/
	
	
}
