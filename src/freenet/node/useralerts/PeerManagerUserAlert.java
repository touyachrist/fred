package freenet.node.useralerts;

import freenet.node.Node;

public class PeerManagerUserAlert implements UserAlert {

	final Node n;
	public int conns;
	public int peers;
	boolean isValid=true;
	
	public PeerManagerUserAlert(Node n) {
		this.n = n;
	}
	
	public boolean userCanDismiss() {
		return false;
	}

	public String getTitle() {
		if(peers == 0)
			return "No peers found";
		if(conns == 0)
			return "No open connections";
		if(conns == 1)
			return "Only 1 open connection";
		if(conns == 2)
			return "Only 2 open connections";
		else throw new IllegalArgumentException("Not valid");
	}
	
	public String getText() {
		String s;
		if(peers == 0) {
			s = "This node has no peers to connect to, therefore it will not " +
			"be able to function normally. Ideally you should connect to peers run by people you know " +
			"(if you are paranoid, then people you trust; if not, then at least people you've talked to)";
			String end = " log on to irc.freenode.net channel #freenet-refs and ask around for somebody to connect to";
			if(n.isTestnetEnabled())
				s += ", but since this is a testnet node, we suggest that you " + end + ".";
			else
				s += ". You could " + end + ", but remember that you are vulnerable to " +
				"those you are directly connected to. (This is especially true in this early alpha of Freenet 0.7...)<br/>BE SURE THAT THE OTHER PERSON HAS ADDED YOUR REFERENCE, TOO, AS ONE-WAY CONNECTIONS WON'T WORK!";
		}else if(peers-conns >= 20){ 
			s = "This node has too many disconnected peers : it will have a negative impact on your performance! Consider \"cleaning up\" your peer list.";
		}else if(conns == 0) {
			s = "This node has not been able to connect to any other nodes so far; it will not be able to function normally. " +
			"Hopefully some of your peers will connect soon; if not, try to get some more peers.";
		} else if(conns == 1) {
			s = "This node only has one connection. Performance will be impaired, and you have no anonymity nor even plausible deniability if that one person is malicious. " +
			"Your node is attached to the network like a 'leaf' and does not contribute to the network's health." +
			"Try to get at least 3 connected peers at any given time.";
		} else if(conns == 2) {
			s = "This node has only two connections. Performance and security will not be very good, and your node is not doing any routing for other nodes. " +
			"Your node is embedded like a 'chain' in the network and does not contribute to the network's health." +
			"Try to get at least 3 connected peers at any given time.";
		}else if(conns >= 30) {
			s = "This node many connections. We don't encourage such a behaviour : Ubernodes are hurting the network.";
		} else throw new IllegalArgumentException("Not valid");
		return s;
	}

	public short getPriorityClass() {
		if(peers == 0 || conns == 0 || conns >= 20)
			return UserAlert.CRITICAL_ERROR;
		return UserAlert.ERROR;
	}

	public boolean isValid() {
		return (peers == 0 || conns <= 2)&&isValid;
	}
	
	public void isValid(boolean b){
		if(userCanDismiss()) isValid=b;
	}
	
	public String dismissButtonText(){
		return "Hide";
	}
	
	public boolean shouldUnregisterOnDismiss() {
		return false;
	}
}
