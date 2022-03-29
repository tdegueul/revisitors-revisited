import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.joining;

class Machine { Collection<State> states; }
class State { String name; Collection<Transition> out;  }
class FinalState extends State {}
class Transition { State target; String event; }

interface FsmAlg<M, S, F extends S, T> {
	M machine(Machine it);
	S state(State it);
	F finalState(FinalState it);
	T transition(Transition it);

	default M $(Machine it) { return machine(it); }
	default F $(FinalState it) { return finalState(it); }
	default T $(Transition it) { return transition(it); }
	default S $(State it) {
		return switch (it) {
			case FinalState fs -> finalState(fs);
			case State s -> state(s);
		};
	}
}

interface Print {
	String print();
}

interface PrintFsm extends FsmAlg<Print, Print, Print, Print> {
	default Print machine(Machine it) {
		return () -> it.states.stream().map(s -> $(s).print())
			.collect(joining("\n"));
	}
	default Print state(State it) {
		return () -> it.name + "\n" + it.out.stream().map(t -> $(t).print()).collect(joining("\n"));
	}
	default Print finalState(FinalState it) {
		return () -> "*" + state(it).print();
	}
	default Print transition(Transition it) {
		return () -> it.event + "=>" + it.target.name;
	}
}

public class FsmRevisitorRevisited {
	public static void main(String[] args) {
		State s1 = new State();
		s1.name = "s1";
		FinalState s2 = new FinalState();
		s2.name = "s2";
		Transition t = new Transition();
		t.event = "a";
		t.target = s2;
		s1.out = Collections.singletonList(t);
		s2.out = Collections.emptyList();
		Machine fsm = new Machine();
		fsm.states = List.of(s1, s2);

		Print p = new PrintFsm(){}.$(fsm);
		System.out.println(p.print());
	}
}