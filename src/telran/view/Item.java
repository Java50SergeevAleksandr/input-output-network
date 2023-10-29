package telran.view;

import java.util.function.Consumer;

public interface Item {
	String displayName();

	boolean isExit();

	void perform(InputOutput io);

	static Item of(String name, Consumer<InputOutput> consumer, boolean isExit) {
		return new Item() {

			@Override
			public String displayName() {
				return name;
			}

			@Override
			public boolean isExit() {
				return isExit;
			}

			@Override
			public void perform(InputOutput io) {
				consumer.accept(io);
			}

		};
	}

	static Item of(String name, Consumer<InputOutput> consumer) {
		return of(name, consumer, false);
	}

	static Item exit() {
		return of("Exit", io -> {}, true);
	}
}
