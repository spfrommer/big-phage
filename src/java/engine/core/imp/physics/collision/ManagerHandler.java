package engine.core.imp.physics.collision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

import engine.core.frame.Entity;

public class ManagerHandler implements ContactListener {
	private Map<Entity, List<CollisionEvent>> m_events = new HashMap<Entity, List<CollisionEvent>>();
	private List<CompleteListener> m_listeners = new ArrayList<CompleteListener>();

	public void addEvent(Entity entity, CollisionEvent event) {
		if (!m_events.containsKey(entity)) {
			m_events.put(entity, new ArrayList<CollisionEvent>());
		}
		m_events.get(entity).add(event);
	}

	public void removeEvent(Entity entity, CollisionEvent event) {
		m_events.get(entity).remove(event);
	}

	public void addCompleteListener(CompleteListener listener) {
		m_listeners.add(listener);
	}

	public void removeCompleteListener(CompleteListener listener) {
		m_listeners.remove(listener);
	}

	@Override
	public void beginContact(Contact contact) {
		Entity entity1 = (Entity) contact.getFixtureA().getBody().getUserData();
		Entity entity2 = (Entity) contact.getFixtureB().getBody().getUserData();

		if (entity1 != null && m_events.containsKey(entity1)) {
			for (CollisionEvent e : m_events.get(entity1))
				e.collidedWith(entity2);
		}
		if (entity2 != null && m_events.containsKey(entity2)) {
			for (CollisionEvent e : m_events.get(entity2))
				e.collidedWith(entity1);
		}

		for (CompleteListener listener : m_listeners)
			listener.beginContact(contact);
	}

	@Override
	public void endContact(Contact contact) {
		for (CompleteListener listener : m_listeners)
			listener.endContact(contact);
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		for (CompleteListener listener : m_listeners)
			listener.postSolve(contact, impulse);
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		for (CompleteListener listener : m_listeners)
			listener.preSolve(contact, oldManifold);
	}
}
