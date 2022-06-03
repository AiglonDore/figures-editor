package history;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import logger.LoggerFactory;

/**
 * Undo / Redo manager for elements of type E
 * @author davidroussel
 * @param <E> The type of objects to store into {@link #undoStack} and
 * {@link #redoStack}
 */
public class HistoryManager<E extends Prototype<E>>
{
	/**
	 * Maximum number of elements in {@link #undoStack} or {@link #redoStack}
	 */
	private int size;

	/**
	 * The {@link Originator} which state should be stored.
	 * The Originator shoud be able to create a {@link Memento} saving its
	 * internal state and also setup a new state according to a provided
	 * {@link Memento}
	 * @see Originator#createMemento()
	 * @see Originator#setMemento(Memento)
	 */
	private Originator<E> originator;

	/**
	 * Undo stack
	 * @note {@link Deque} allow to push / pop elements but also to access its
	 * last element in order to maintain a {@link #size} below a max number
	 * @see #redoStack
	 */
	private Deque<Memento<E>> undoStack;

	/**
	 * Redo stack
	 * @see #undoStack
	 */
	private Deque<Memento<E>> redoStack;

	/**
	 * The logger to use for debug messsages
	 */
	private Logger logger;

	/**
	 * Undo / Redo manager constructor
	 * @param origin the {@link Originator} to manage
	 * @param size the maximum number of states to save in {@link #undoStack} &
	 * @param parentLogger the Parent Logger to issue messages
	 * {@link #redoStack}
	 */
	public HistoryManager(Originator<E> origin, int size, Logger parentLogger)
	{
		this.size = size;
		originator = origin;
		undoStack = new LinkedList<Memento<E>>();
		redoStack = new LinkedList<Memento<E>>();
		logger = LoggerFactory.getParentLogger(getClass(),
		                                       parentLogger,
		                                       (parentLogger == null ?
		                                    	Level.INFO : null)); // null level to inherit parent logger's level
	}

	/**
	 * Finalization.
	 * Clears {@link #undoStack} & {@link #redoStack} to make GC life easier.
	 */
	@Override
	protected void finalize() throws Throwable
	{
		undoStack.clear();
		redoStack.clear();
	}

	/**
	 * Number of elements in {@link #undoStack}
	 * @return the number of elements in {@link #undoStack}
	 */
	public int undoSize()
	{
		return undoStack.size();
	}

	/**
	 * Number of elements in {@link #redoStack}
	 * @return the number of elements in {@link #redoStack}
	 */
	public int redoSize()
	{
		return redoStack.size();
	}

	/**
	 * Resize {@link #undoStack} & {@link #redoStack} to new Size.
	 * If current size > provided size, {@link #undoStack} & {@link #redoStack}
	 * are trimmed to provided size, or left unchanged otherwise.
	 * @param size the new Undo / rEdo stack sizes
	 */
	public void setSize(int size)
	{
		if (this.size > size)
		{
			// TODO HistoryManager#setSize: Trim #undoStack & #redoStack if required
		}
	}

	/**
	 * Records a {@link Memento} from the {@link #originator} into
	 * {@link #undoStack} and clears the {@link #redoStack}.
	 * This is the default method to use when an action that might change the
	 * state of the {@link #originator} begins.
	 * It can be cancelled later with {@link #cancel()} if the state of the
	 * {@link #originator} hasn't changed.
	 * @see #pushUndo(Memento)
	 */
	public void record()
	{
		// TODO HistoryManager#record ...
	}

	/**
	 * Push {@link #originator}'s current state into {@link #redoStack} and
	 * Pops the last state from {@link #undoStack} into {@link #originator}
	 * iff non null.
	 * @see #pushRedo(Memento)
	 * @see Originator#createMemento()
	 * @see #popUndo()
	 * @see Originator#setMemento(Memento)
	 */
	public void undo()
	{
		// TODO HistoryManager#undo ...
	}

	/**
	 * Cancel the last {@link Memento} saved in {@link #undoStack} by popping it.
	 * To be used when an action didn't modify the current state (by being
	 * cancelled for instance).
	 * @see #popUndo()
	 */
	public void cancel()
	{
		popUndo();
	}

	/**
	 * Push {@link Originator}'s current state into {@link #undoStack} and
	 * pops the last {@link Memento} from {@link #redoStack} into
	 * {@link Originator} iff not null.
	 * @see #pushUndo(Memento)
	 * @see Originator#createMemento()
	 * @see #popRedo()
	 * @see Originator#setMemento(Memento)
	 */
	public void redo()
	{
		// TODO HistoryManager#redo ...
	}

	/**
	 * {@link #undoStack} and {@link #redoStack} display method
	 * @return A new string containing both stacks content.
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		sb.append(super.toString());
		sb.append("[" + String.valueOf(size) + "] :\nUndo = {");
		for (Iterator<Memento<E>> it = undoStack.iterator(); it.hasNext();)
		{
			sb.append(it.next());
			if (it.hasNext())
			{
				sb.append(", ");
			}
		}
		sb.append("},\nRedo = {");
		for (Iterator<Memento<E>> it = redoStack.iterator(); it.hasNext();)
		{
			sb.append(it.next());
			if (it.hasNext())
			{
				sb.append(", ");
			}
		}
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Push a new {@link Memento} (iff non null and not similar to last pushed
	 * Memento) to the {@link #undoStack}
	 * @param state the {@link Memento} to push
	 * @return true if the {@link Memento} was non null, different from the
	 * last pushed {@link Memento} and was pushed to {@link #undoStack},
	 * false otherwise.
	 * @see Deque#peekFirst()
	 * @see Deque#push(Object)
	 * @see Deque#size()
	 * @see Deque#removeLast()
	 */
	private boolean pushUndo(Memento<E> state)
	{
		// TODO HistoryManager#pushUndo ...
		return false;
	}

	/**
	 * Pops the last element pushed into {@link #undoStack}
	 * @return the state on top of the {@link #undoStack} or null if there was
	 * no states in {@link #undoStack}
	 * @see Deque#size()
	 * @see Deque#pop()
	 */
	private Memento<E> popUndo()
	{
		Memento<E> state = null;

		// TODO HistoryManager#popUndo ...

		return state;
	}

	/**
	 * Push the provided state into {@link #redoStack} iff the provided state is
	 * not null and different from last pushed state. If the resulting number of
	 * states is > to {@link #size}, then the first pushed state into
	 * {@link #redoStack} is removed.
	 * @param state the state to push into {@link #redoStack}
	 * @return true if the provided state has been added to {@link #redoStack},
	 * false otherwise
	 * @see Deque#peekFirst()
	 * @see Deque#push(Object)
	 * @see Deque#size()
	 * @see Deque#removeLast()
	 */
	private boolean pushRedo(Memento<E> state)
	{
		// TODO HistoryManager#pushRedo ...
		return false;
	}

	/**
	 * Pops the last element pushed into {@link #redoStack}
	 * @return the state on top of the {@link #redoStack} or null if there was
	 * no states in {@link #redoStack}
	 * @see Deque#size()
	 * @see Deque#pop()
	 */
	private Memento<E> popRedo()
	{
		Memento<E> state = null;

		// TODO HistoryManager#popRedo ...

		return state;
	}
}
