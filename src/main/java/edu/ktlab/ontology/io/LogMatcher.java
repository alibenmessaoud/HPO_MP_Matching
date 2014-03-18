package edu.ktlab.ontology.io;

import java.io.PrintStream;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import uk.ac.ox.krr.logmap2.LogMap2_Matcher;
import uk.ac.ox.krr.logmap2.mappings.objects.MappingObjectStr;

public class LogMatcher {
	OWLOntology onto1;
	OWLOntology onto2;

	OWLOntologyManager onto_manager;
	
	public LogMatcher(){
		try{
			String onto1_iri = "file:ontology/hp1.owl";
			String onto2_iri = "file:ontology/mp1.owl";
			
			onto_manager = OWLManager.createOWLOntologyManager();
			
			onto1 = onto_manager.loadOntology(IRI.create(onto1_iri));
			onto2 = onto_manager.loadOntology(IRI.create(onto2_iri));
			
			System.out.println("Load successfully!");
			
			LogMap2_Matcher logmap2 = new LogMap2_Matcher(onto1, onto2);
			//Optionally LogMap also accepts the IRI strings as input 
			//LogMap2_Matcher logmap2 = new LogMap2_Matcher(onto1_iri, onto2_iri);
			
			//Set of mappings computed my LogMap
			Set<MappingObjectStr> logmap2_mappings = logmap2.getLogmap2_Mappings();
			
			System.out.println("Number of mappings computed by LogMap: " + logmap2_mappings.size());
			
			PrintStream out = new PrintStream("logmap.output");
			
			for(MappingObjectStr oStr: logmap2_mappings){
				double score = oStr.getConfidence();
				String hpId = getId(oStr.getIRIStrEnt1());
				String mpId = getId(oStr.getIRIStrEnt2());
				out.println(hpId + "\t" + mpId + "\t" + score);
			}
			
			out.close();
			/*
			 * Accessing mapping objects
			 *  
			for (MappingObjectStr mapping: logmap2_mappings){
				System.out.println("Mapping: ");
				System.out.println("\t"+ mapping.getIRIStrEnt1());
				System.out.println("\t"+ mapping.getIRIStrEnt2());
				System.out.println("\t"+ mapping.getConfidence());
				
				//MappingObjectStr.EQ or MappingObjectStr.SUB or MappingObjectStr.SUP
				System.out.println("\t"+ mapping.getMappingDirection()); //Utilities.EQ;
				
				//MappingObjectStr.CLASSES or MappingObjectStr.OBJECTPROPERTIES or MappingObjectStr.DATAPROPERTIES or MappingObjectStr.INSTANCES
				System.out.println("\t"+ mapping.getTypeOfMapping());
				
			}*/
			
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private String getId(String iri){
		int idx = iri.lastIndexOf("/");
		String id = iri.substring(idx + 1, iri.length());
		id = id.replace("_", ":");
		return id;
	}
	
	public static void main(String[] args){
		new LogMatcher();
	}
}
