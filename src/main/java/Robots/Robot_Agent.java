package Robots;

import java.util.Random;

import commonFunctionality.CommonAgent;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.lang.acl.ACLMessage;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;

public class Robot_Agent extends CommonAgent {
	
	@Override
	protected void setup() {
		/* forming service description */
		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setName("boiling");
		serviceDescription.setType("Coffee-machine");
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
		addBehaviour(new HandleCallForProposal(this));
	}
	
	class HandleCallForProposal extends ContractNetResponder {
		private static final long serialVersionUID = 2429876704345890795L;

		public HandleCallForProposal(Agent a) {
			super(a, createMessageTemplate(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET));
		}

		@Override
		protected ACLMessage handleCfp(ACLMessage cfp) throws NotUnderstoodException, RefuseException {
			ACLMessage reply = cfp.createReply();
			reply.setPerformative(ACLMessage.PROPOSE);
			reply.setContent("ok, master");
			return reply;
		}

		@Override
		protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept)
				throws FailureException {
			ACLMessage inform = accept.createReply();
			inform.setPerformative(ACLMessage.INFORM);

			System.out.println("printing...");
			return inform;
		}
	}

	/* This Behaviour handles "accept-proposal" messages */
	class HandleAcceptProposal extends CyclicBehaviour {
		private static final long serialVersionUID = 8759104857697556076L;

		@Override
		public void action() {
			/*
			 * only message containg ACCEPT-PROPOSAL in "performative" slot will be
			 * processed
			 */
			MessageTemplate msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
			ACLMessage msg = receive(msgTemplate);
			if (msg != null) {
				/* create reply for incoming message */
				ACLMessage reply = msg.createReply();
				reply.setPerformative(ACLMessage.INFORM);

				System.out.println("printing...");
				/* send reply for incoming message */
				send(reply);
			} else {
				/* wait till there is message matching template in message-queue */
				block();
			}

		}
	}

}
