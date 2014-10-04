package engine.core.imp.physics.liquid;

import java.util.ArrayList;
import java.util.List;

import engine.commons.utils.BiMap;
import engine.commons.utils.Vector2f;

public class GridManager {
	private List<Grid> m_gridList = new ArrayList<Grid>();
	private BiMap<Object, Grid> m_grids = new BiMap<Object, Grid>();
	private Vector2f m_gridDimensions;

	public GridManager(Vector2f gridDimensions) {
		m_gridDimensions = gridDimensions;
	}

	public Grid getGrid(Vector2f position) {
		return getGrid((int) Math.floor(position.x / m_gridDimensions.x),
				(int) Math.floor(position.y / m_gridDimensions.y));
	}

	public Grid getGrid(int x, int y) {
		Object hash = hash(x, y);

		if (!m_grids.containsKey(hash)) {
			Grid grid = new Grid(new Vector2f(m_gridDimensions.x * x, m_gridDimensions.y * y), m_gridDimensions);
			m_grids.put(hash, grid);
			m_gridList.add(grid);
		}

		return m_grids.getForward(hash);
	}

	public List<Grid> getGrids() {
		return m_gridList;
	}

	public void removeGrid(Grid g) {
		m_gridList.remove(g);
		m_grids.removeBackward(g);
	}

	private static Object hash(int x, int y) {
		return new Pair<Integer, Integer>(x, y);
	}

	public static void main(String[] args) {
		GridManager manager = new GridManager(new Vector2f(0.5f, 0.5f));
		Grid g = manager.getGrid(new Vector2f(1f, 1f));
		System.out.println(g.isInGrid(new Vector2f(1f, 1f)));
	}
}
