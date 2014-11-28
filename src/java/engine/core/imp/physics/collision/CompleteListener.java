package engine.core.imp.physics.collision;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

public interface CompleteListener {
	public void beginContact(Contact contact);

	public void endContact(Contact contact);

	public void postSolve(Contact contact, ContactImpulse impulse);

	public void preSolve(Contact contact, Manifold oldManifold);
}
