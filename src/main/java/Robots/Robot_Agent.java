package Robots;

import commonFunctionality.CommonAgent;
import jade.core.Agent;
import jade.domain.DFService;
import jade.lang.acl.ACLMessage;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.MessageTemplate;

public class Robot_Agent extends CommonAgent {
	
	@Override
	protected void setup() {
		/* forming service description */
		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setName("printing");
		serviceDescription.setType("office-service");
		/* forming agent description. one agent can provide several services */
		DFAgentDescription agentDescription = new DFAgentDescription();
		agentDescription.setName(getAID());
		agentDescription.addServices(serviceDescription);
		try {
			/* registering agent description in DF-Agent */
			DFService.register(this, agentDescription);
		} catch (FIPAException exception) {
			exception.printStackTrace();
		}

		/* registering Behaviours to react for different types of messages */
		addBehaviour(new HandleAcceptProposal());
		addBehaviour(new HandleCallForProposal());
	}
	
}
