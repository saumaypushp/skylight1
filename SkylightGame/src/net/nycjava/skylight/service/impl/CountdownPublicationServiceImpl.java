package net.nycjava.skylight.service.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import net.nycjava.skylight.service.CountdownObserver;
import net.nycjava.skylight.service.CountdownPublicationService;

public class CountdownPublicationServiceImpl implements CountdownPublicationService {

	private int duration = 0;

	private int currentCount = 0;

	private boolean stopRequested = false;

	private Set<CountdownObserver> countdownObservers = new HashSet<CountdownObserver>();

	private CounterStatus currentStatus = CounterStatus.uninitialized;

	private Timer countdownTimer;

	public void addObserver(CountdownObserver anObserver) {
		countdownObservers.add(anObserver);
		if (this.currentStatus == CounterStatus.running) {
			int remain = getRemainingTime();
			notifyObservers(remain);
		}
	}

	public boolean removeObserver(CountdownObserver anObserver) {
		final boolean existed = countdownObservers.remove(anObserver);
		return existed;
	}

	public void setDuration(int seconds) {
		duration = seconds;
	}

	private int getRemainingTime() {
		int aRemainingTime = this.duration - currentCount;
		return aRemainingTime;
	}

	class CountdownTask extends TimerTask {
		public void run() {
			if (duration == 0)
				return;
			currentStatus = CounterStatus.running;
			if (currentCount < duration && stopRequested == false) {
				currentCount = currentCount + 1;
				notifyObservers(getRemainingTime());
//				System.out.println("start counting: " + currentCount);

			}
			if (currentCount == duration) {
				currentStatus = CounterStatus.finished;
			}

			if (stopRequested) {
				currentStatus = CounterStatus.stopped;
			}
		}
	}

	public void startCountdown() {
		if (this.duration == 0) {
			// should be an assertion here
			return;
		}

		else if (this.currentStatus == CounterStatus.running) {
			// should assert here as well
			return;
		}
		countdownTimer = new Timer();
		CountdownTask countdownTask = new CountdownTask();
		long zeroDelay = 0;
		countdownTimer.scheduleAtFixedRate(countdownTask, zeroDelay, 1000);
	}

	public void stopCountdown() {
		stopRequested = true;
		if (countdownTimer != null) {
			countdownTimer.cancel();
		}
		currentStatus = CounterStatus.stopped;
	}

	private void notifyObservers(int aRemainingTime) {

		for (CountdownObserver countdownObserver : countdownObservers) {
			countdownObserver.countdownNotification(aRemainingTime);
		}
	}

	@Override
	public CounterStatus getStatus() {
		return currentStatus;
	}

}