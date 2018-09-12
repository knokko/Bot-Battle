package nl.knokko.client.connection.protocol.main;

import nl.knokko.client.connection.listener.ReceiveProtocol;
import nl.knokko.client.gui.menu.MenuRegister;
import nl.knokko.connection.ConnectionCode;
import nl.knokko.util.bits.BitInput;

public class ProtocolRegister implements ReceiveProtocol {

	@Override
	public void process(BitInput input) {
		boolean result = input.readBoolean();
		if(result){
			//TODO account has been created, it will begin with default values
		}
		else {
			byte reason = input.readByte();//registering failed
			MenuRegister.getInstance().setError(ConnectionCode.StC.Auth.RegisterFail.asString(reason));
		}
	}
}