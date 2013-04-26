package yadroneTest;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import wiiusej.WiiUseApiManager;
import wiiusej.Wiimote;
import wiiusej.wiiusejevents.physicalevents.ExpansionEvent;
import wiiusej.wiiusejevents.physicalevents.IREvent;
import wiiusej.wiiusejevents.physicalevents.JoystickEvent;
import wiiusej.wiiusejevents.physicalevents.MotionSensingEvent;
import wiiusej.wiiusejevents.physicalevents.NunchukButtonsEvent;
import wiiusej.wiiusejevents.physicalevents.NunchukEvent;
import wiiusej.wiiusejevents.physicalevents.WiimoteButtonsEvent;
import wiiusej.wiiusejevents.utils.WiimoteListener;
import wiiusej.wiiusejevents.wiiuseapievents.ClassicControllerInsertedEvent;
import wiiusej.wiiusejevents.wiiuseapievents.ClassicControllerRemovedEvent;
import wiiusej.wiiusejevents.wiiuseapievents.DisconnectionEvent;
import wiiusej.wiiusejevents.wiiuseapievents.GuitarHeroInsertedEvent;
import wiiusej.wiiusejevents.wiiuseapievents.GuitarHeroRemovedEvent;
import wiiusej.wiiusejevents.wiiuseapievents.NunchukInsertedEvent;
import wiiusej.wiiusejevents.wiiuseapievents.NunchukRemovedEvent;
import wiiusej.wiiusejevents.wiiuseapievents.StatusEvent;

import com.shigeodayo.ardrone.ARDrone;
import com.shigeodayo.ardrone.navdata.BatteryListener;

public class Test implements ActionListener, KeyListener, WiimoteListener, BatteryListener{
	
	private ARDrone drone;
	private JFrame frame;;
	private JPanel panel;
	private final int HEIGHT = 800;
	private final int WIDTH = 800;
	private final Dimension frameDim = new Dimension(WIDTH, HEIGHT);
	
	JButton connectButton;
	JButton disconnectButton;
	JButton connWiimoteButton;
	JButton disconnWiimoteButton;
	JButton takeoffButton;
	JButton landButton;
	JButton resetButton;
	
	JButton fwd_Button;
	
	
	
	private Wiimote[] wiiMotes;
	
	
	
	public Test(){
		
		drone = new ARDrone("192.168.1.1");
		makeFrame();
			
	}
	
	
	public void regWiiMotes(){
		wiiMotes = WiiUseApiManager.getWiimotes(4, true);
		for(int idx = 0; idx < wiiMotes.length; idx++){
			Wiimote wiiMote = wiiMotes[idx];
			switch(idx){
			case 0:
				wiiMote.setLeds(true, false, false, false);
				break;
			case 1:
				wiiMote.setLeds(false, true, false, false);
				break;
			case 2:
				wiiMote.setLeds(false, false, true, false);
				break;
			case 3:
				wiiMote.setLeds(false, false, false, true);
				break;
			}
			System.out.println("Wii-mote ID: " + wiiMote.getId());
			wiiMote.activateMotionSensing();
			wiiMote.activateSmoothing();
			wiiMote.addWiiMoteEventListeners(this);	
		}
		
	}
	
	public void disconnectWiiMotes(){
		for(int idx = 0; idx < wiiMotes.length; idx++){
			wiiMotes[idx].disconnect();
		}
	}
	
	public void makeFrame(){
		
		frame = new JFrame("ARDrone Control");
		frame.setPreferredSize(frameDim);
		frame.setResizable(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		
		panel = new JPanel(new GridLayout(5,3));
		
		
		connectButton = new JButton("Connect");
		connectButton.addActionListener(this);
		disconnectButton = new JButton("Disconnect");
		disconnectButton.addActionListener(this);
		
		connWiimoteButton = new JButton("Register Wiimotes");
		connWiimoteButton.addActionListener(this);
		disconnWiimoteButton = new JButton("Disconnect Wiimotes");
		disconnWiimoteButton.addActionListener(this);
		
		takeoffButton = new JButton("Take off!");
		takeoffButton.addActionListener(this);
		landButton = new JButton("Land");
		landButton.addActionListener(this);
		
		resetButton = new JButton("Rest");
		resetButton.addActionListener(this);
		
		fwd_Button = new JButton("Forward");
		fwd_Button.addActionListener(this);
		
		
		panel.add(connectButton);
		panel.add(disconnectButton);
		panel.add(connWiimoteButton);
		panel.add(disconnWiimoteButton);
		panel.add(takeoffButton);
		panel.add(landButton);
		panel.add(resetButton);
		panel.add(fwd_Button);
		
		frame.add(panel);
		
		frame.isFocusable();
		frame.requestFocusInWindow();
		frame.addKeyListener(this);
		
		
		
	}
	
	public static void main(String[] args){
		
		new Test();
		
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object src = e.getSource();
		if( src == connectButton){
				drone.connect();
				drone.start();
		}
		
		if( src == disconnectButton){
			//drone.stop();
				drone.disconnect();
		}
		
		if( src == connWiimoteButton){
			regWiiMotes();
		}
		
		if( src == disconnWiimoteButton){
			disconnectWiiMotes();
		}
		
		if( src == takeoffButton){
			drone.takeOff();
		}
		
		if( src == landButton){
			drone.landing();
		}
		
		if( src == resetButton){
			drone.reset();
		}
		
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		drone.stop();
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onButtonsEvent(WiimoteButtonsEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.isButtonUpJustPressed()){
			drone.forward(150);
			System.out.println("forward");
		}
		if (arg0.isButtonDownJustPressed()){
			drone.backward(150);
			System.out.println("backward");
		}
		if (arg0.isButtonLeftJustPressed()){
			drone.goLeft(150);
			System.out.println("left");
		}
		if (arg0.isButtonRightJustPressed()){
			drone.goRight(150);
			System.out.println("right");
		}
		if (arg0.isButtonMinusJustPressed()){
			drone.landing();
			System.out.println("land");
		}
		if (arg0.isButtonPlusJustReleased()){
			drone.takeOff();
			System.out.println("take off");
		}
		if (arg0.isButtonHomeJustReleased()){
			drone.reset();
			System.out.println("reset");
		}
		if(arg0.isButtonOneJustPressed()){
			drone.up(50);
			System.out.println("Up");
		}
		if(arg0.isButtonTwoJustPressed()){
			drone.down(50);
			System.out.println("Down");
		}
			


		if (arg0.isButtonUpJustReleased()){
			drone.stop();
			System.out.println("forward");
		}
		if (arg0.isButtonDownJustReleased()){
			drone.stop();
			System.out.println("backward");
		}
		if (arg0.isButtonLeftJustReleased()){
			drone.stop();
			System.out.println("left");
		}
		if (arg0.isButtonRightJustReleased()){
			drone.stop();
			System.out.println("right");
		}
		if (arg0.isButtonOneJustReleased()){
			drone.stop();
			System.out.println("Up");
		}
		if (arg0.isButtonTwoJustReleased()){
			drone.stop();
			System.out.println("Down");
		}	
		
	}

	@Override
	public void onClassicControllerInsertedEvent(
			ClassicControllerInsertedEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClassicControllerRemovedEvent(
			ClassicControllerRemovedEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDisconnectionEvent(DisconnectionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onExpansionEvent(ExpansionEvent arg0) {
		NunchukEvent nnEvent = (NunchukEvent)arg0;
		JoystickEvent nnJsEvent = nnEvent.getNunchukJoystickEvent();
		NunchukButtonsEvent nnBtnEvent = nnEvent.getButtonsEvent();
		if(nnBtnEvent.isButtonCJustPressed()){
			drone.spinLeft(150);
			System.out.println("SpinLeft");
		}
		if(nnBtnEvent.isButtonZJustPressed()){
			drone.spinRight(150);
			System.out.println("SpinRight");
		}
		
		if(nnBtnEvent.isButtonCJustReleased()){
			drone.stop();
			System.out.println("SpinLeft");
		}
		if(nnBtnEvent.isButtonZJustReleased()){
			drone.stop();
			System.out.println("SpinRight");
		}
		
		System.out.println("-------\n" + "Angle: " + Math.round(nnJsEvent.getAngle()) + "\n" +"Magnitude: " + Math.round(nnJsEvent.getMagnitude()*100) + "%" + "\n-------");
		//System.out.println(nnJsEvent.toString());
		
		
		
		
		
	}

	@Override
	public void onGuitarHeroInsertedEvent(GuitarHeroInsertedEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGuitarHeroRemovedEvent(GuitarHeroRemovedEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onIrEvent(IREvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMotionSensingEvent(MotionSensingEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onNunchukInsertedEvent(NunchukInsertedEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNunchukRemovedEvent(NunchukRemovedEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusEvent(StatusEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void batteryLevelChanged(int arg0) {
		// TODO Auto-generated method stub
		System.out.println(arg0);		
	}


}
