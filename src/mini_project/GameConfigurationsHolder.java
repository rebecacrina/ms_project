package mini_project;

public class GameConfigurationsHolder {

	private static final GameConfiguration GC_EASY = new GameConfiguration(10, 10, 10, 300, 346);
	private static final GameConfiguration GC_MEDIUM = new GameConfiguration(16, 16, 35, 496, 540);
	private static final GameConfiguration GC_HARD = new GameConfiguration(16, 30, 75, 780, 492);

	static final GameConfiguration[] GCs = { GC_EASY, GC_MEDIUM, GC_HARD };

}
