package hu.bme.mit.train.controller;

import java.util.Calendar;
import java.util.Timer;

import hu.bme.mit.train.interfaces.TrainController;
import hu.bme.mit.train.tachograph.Tachograph;

public class TrainControllerImpl implements TrainController {

	private int step = 0;
	private int referenceSpeed = 0;
	private int speedLimit = 0;
	private Tachograph t=new Tachograph();

	@Override
	public void followSpeed() {
		if (referenceSpeed < 0) {
			referenceSpeed = 0;
		} else {
		    if(referenceSpeed+step > 0) {
                referenceSpeed += step;
            } else {
		        referenceSpeed = 0;
            }
		}

		enforceSpeedLimit();
		t.record(System.currentTimeMillis(), step, referenceSpeed);
	}

	@Override
	public int getReferenceSpeed() {
		return referenceSpeed;
	}
	
	public int getSpeedLimit() {
		return speedLimit;
	}
	
	public int getTachoSize(){
		return t.getSize();
	}

	@Override
	public void setSpeedLimit(int speedLimit) {
		this.speedLimit = speedLimit;
		enforceSpeedLimit();
		
	}

	private void enforceSpeedLimit() {
		if (referenceSpeed > speedLimit) {
			referenceSpeed = speedLimit;
		}
	}

	@Override
	public void setJoystickPosition(int joystickPosition) {
		this.step = joystickPosition;
		Timer t=new Timer();
		t.scheduleAtFixedRate(new TimerTask() {
			  @Override
			  public void run() {
				  followSpeed();
			  }
			}, 0, 1000);
	}

}
