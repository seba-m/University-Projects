package com.pruebas.modelo.fotos;

import java.io.File;
import java.util.concurrent.CountDownLatch;

import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressEventType;
import com.amazonaws.event.ProgressListener;

class UploadCompleteListener implements ProgressListener {

	CountDownLatch doneSignal;
	File f;
	String target;

	public UploadCompleteListener(File f, String target, CountDownLatch doneSignal) {
		this.f = f;
		this.target = target;
		this.doneSignal = doneSignal;
	}

	public void progressChanged(ProgressEvent progressEvent) {
		if (progressEvent.getEventType() == ProgressEventType.TRANSFER_STARTED_EVENT) {
		}
		if (progressEvent.getEventType() == ProgressEventType.TRANSFER_COMPLETED_EVENT) {
			doneSignal.countDown();
		}
		if (progressEvent.getEventType() == ProgressEventType.TRANSFER_FAILED_EVENT) {
			doneSignal.countDown();
		}
	}
}