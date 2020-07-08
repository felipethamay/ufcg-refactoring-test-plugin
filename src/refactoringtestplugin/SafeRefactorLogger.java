package refactoringtestplugin;

import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.Project;

public class SafeRefactorLogger implements BuildListener {

	private long before;
	private long after;
	private long total;
	private String taskName;
	
	private long instrumentTime = 0;
	private long compileSource = 0;
	private long compileTarget = 0;
	private long generateTests = 0;
	private long compileTests = 0;
	private long runTests = 0;
	private long coverage = 0;
	/**
	 * <p>
	 * Signals that a build has started. This event is fired before any targets
	 * have started.
	 * </p>
	 * 
	 * @param start
	 *            An event with any relevant extra information. Must not be
	 *            <code>null</code>.
	 */
	public final void buildStarted(final BuildEvent start) {
		start.getProject().log("buildStarted() called.", Project.MSG_ERR);
		
		

	}
	
	public String getResults() {
		StringBuffer sb = new StringBuffer();		
		sb.append("\nTempo para compilar source: " + compileSource/1000);
		sb.append("\nTempo para compilar target " + compileTarget/1000);
		sb.append("\nTempo para gerar testes " + generateTests/1000);
		sb.append("\nTempo para compilar testes " + compileTests/1000);
		sb.append("\nTempo para instrumentar o codigo " + instrumentTime/1000);
		sb.append("\nTempo para rodar os testes " + runTests/1000);
		sb.append("\nTempo para rodar analisar cobertura " + coverage/1000);
		return sb.toString();
	}

	/**
	 * <p>
	 * Signals that the last target has finished. This event will still be fired
	 * if an error occurred during the build.
	 * </p>
	 * 
	 * @param finish
	 *            An event with any relevant extra information. Must not be
	 *            <code>null</code>.
	 * 
	 * @see BuildEvent#getException()
	 */
	public final void buildFinished(final BuildEvent finish) {
		finish.getProject().log("buildFinished() called.", Project.MSG_ERR);
	}

	/**
	 * <p>
	 * Signals that a target is starting.
	 * </p>
	 * 
	 * @param start
	 *            An event with any relevant extra information. Must not be
	 *            <code>null</code>.
	 * 
	 * @see BuildEvent#getTarget()
	 */
	public final void targetStarted(final BuildEvent start) {
		start.getProject().log(
				"Target [" + start.getTarget().getName() + "] started.",
				Project.MSG_ERR);
		
		taskName = start.getTarget().getName();
		before = System.currentTimeMillis();
		
	}

	/**
	 * <p>
	 * Signals that a target has finished. This event will still be fired if an
	 * error occurred during the build.
	 * </p>
	 * 
	 * @param finish
	 *            An event with any relevant extra information. Must not be
	 *            <code>null</code>.
	 * 
	 * @see BuildEvent#getException()
	 */
	public final void targetFinished(final BuildEvent finish) {
		String taskName = finish.getTarget().getName();
		finish.getProject().log(
				"Target [" + taskName + "] finished.",
				Project.MSG_ERR);
		after = System.currentTimeMillis();		
		logTaskTime();
	}
	
	private void logTaskTime() {
		total = after - before;
		
		if (taskName.equals("instrument"))
			instrumentTime = instrumentTime  + total;
		else if (taskName.equals("compileSource"))
			compileSource = compileSource  + total;
		else if (taskName.equals("compileTarget"))
			compileTarget= compileTarget + total;
		else if (taskName.equals("generate_tests"))
			generateTests = generateTests  + total;
		else if (taskName.equals("compile_tests"))
			compileTests = compileTests + total;
		else if (taskName.equals("run_tests"))
			runTests = runTests + total;		
		else if (taskName.equals("coverage"))
			coverage = coverage + total;		
	}

	/**
	 * <p>
	 * Signals that a task is starting.
	 * </p>
	 * 
	 * @param start
	 *            An event with any relevant extra information. Must not be
	 *            <code>null</code>.
	 * 
	 * @see BuildEvent#getTask()
	 */
	public final void taskStarted(final BuildEvent start) {
		start.getProject().log(
				"Task [" + start.getTask().getTaskName() + "] started.",
				Project.MSG_ERR);
	}

	/**
	 * <p>
	 * Signals that a task has finished. This event will still be fired if an
	 * error occurred during the build.
	 * </p>
	 * 
	 * @param finish
	 *            An event with any relevant extra information. Must not be
	 *            <code>null</code>.
	 * 
	 * @see BuildEvent#getException()
	 */
	public final void taskFinished(final BuildEvent finish) {
		finish.getProject().log(
				"Task [" + finish.getTask().getTaskName() + "] finished.",
				Project.MSG_ERR);
	}

	/**
	 * <p>
	 * When a message is sent to this logger, Ant calls this method.
	 * </p>
	 * 
	 * @param event
	 *            An event with any relevant extra information. Must not be
	 *            <code>null</code>.
	 * 
	 * @see BuildEvent#getMessage()
	 * @see BuildEvent#getPriority()
	 */
	public void messageLogged(final BuildEvent event) {
		// empty
	}

}
