class Saludo {
	boolean claseComenzada;
	
	public Saludo() {
		this.claseComenzada = false;
	}
	
	public synchronized void saludarProfesor() {
		while (!claseComenzada) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public synchronized void llegadaDelProfesor() {
		this.claseComenzada = true;
		this.notifyAll();
	}
}


class Alumno extends Thread {
	
	Saludo saludo;
	String nombre;
	
	public Alumno(String nombre, Saludo saludo) {
		this.saludo = saludo;
		this.nombre = nombre;
	}
	
	@Override
	public void run() {
		System.out.println("Ha llegado el alumno " + this.nombre);
		try {
			Thread.sleep(1000);
			saludo.saludarProfesor();
			System.out.println("(" + this.nombre + ") Bueno días, maestro!");
		} catch (InterruptedException e) {
			System.err.println("Hilo alumno interrumpido");
		}
	}
}

class Profesor extends Thread {
	
	Saludo saludo;
	String nombre;
	
	public Profesor(String nombre, Saludo saludo) {
		this.saludo = saludo;
		this.nombre = nombre;
	}
	@Override
	public void run() {
		System.out.println("Ha llegado el profesor " + this.nombre);
		try {
			Thread.sleep(2000);
			saludo.llegadaDelProfesor();
		} catch (InterruptedException e) {
			System.err.println("Hilo profesor interrumpido");
		}
	}
}

public class ComienzaClase {
	public static void main(String[] args) {
		Saludo saludo = new Saludo();
		int nAlumnos = Integer.parseInt(args[0]);
		
		for (int i = 0; i < nAlumnos; i++) {
			new Alumno("Alumno " + i, saludo).start();
		}
		
		new Profesor("Eliseo Moreno", saludo).start();
	}
}
