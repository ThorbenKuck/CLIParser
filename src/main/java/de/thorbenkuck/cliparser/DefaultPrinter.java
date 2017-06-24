package de.thorbenkuck.cliparser;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class DefaultPrinter implements Printer {

	private final Lock lock;

	DefaultPrinter() {
		lock = new ReentrantLock(true);
	}

	@Override
	public void print(String string) {
		try {
			lock.lock();
			System.out.print(string);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void println(String string) {
		try {
			lock.lock();
			System.out.println(string);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void printError(String string) {
		try {
			lock.lock();
			System.out.println("[ERR]: " + string);
		} finally {
			lock.unlock();
		}
	}
}
