# Genetic
Genetic algorithms experiments

```js
// ---------------------------- DNA -------------------------------//

// How it works:
(
var target = [60, 78, 55, 71, 55];
var size = target.size;
a = DNA(size);
b = DNA(size);
a.calcFitness(target);
"Genes male: %".format(a.genes).postln;
"Genes female: %".format(b.genes).postln;
"Gene: size %".format(a.geneSize).postln;
"Initial fitness: %".format(a.fitness).postln;
x = a.copulate(b);
x.mutate(10);
"Next breed %".format(x.genes).postln;
""
)
```

```js
// ---------------------------- Evolve a simple melody -------------------------------//

(
var target = [60, 65, 68, 71, 67];
var mutationRate = 0.1;
var population = 200;
var creatures;
var matingPool = List.new();
var best, elite = List.new();
var eliteSequence = List.new();

creatures = population.collect{ DNA(target.size) }; // currently only in the range of 36 to 96

t = Task({
	var generation = 0;
	creatures.do{|dna, i| "Initial state %: %".format( i+1, dna.genes).postln };
	inf.do{
		generation = generation + 1;
		creatures.do{|dna| dna.calcFitness(target)};
		population.do{ |i|
			var nth = (creatures[i].fitness * 100); // A multiplier to loop based on fitness. Gets us more of the best ones!
			nth.do{
				matingPool.add(creatures[i]) // The fittest gets in the pool!
			}
		};

		population.do{ |i|
			var pickA = rrand(0, matingPool.size-1);
			var pickB = rrand(0, matingPool.size-1);
			var male = matingPool.at(pickA);
			var female = matingPool.at(pickB);
			var child = male.copulate(female);
			child.mutate(mutationRate);
			creatures[i] = child; //Refresh the creature population
		};

		"".postln;
		"Generation: % ".format(generation).postln;

		best = creatures[0];
		creatures.do{|individual|
			individual.calcFitness(target);
			if (individual.fitness > best.fitness) { best = individual }
		};
		"Fittest value: %".format(best.fitness).postln;
		"Best match: %".format(best.genes).postln;
		elite.add(best);

		population.do{|i| if (creatures[i].getGenes == target) {
			var evolved = creatures[i].getGenes;
			t.stop;
			"-> Elite individuals: ".postln;
			elite.do{|item| eliteSequence.add(item.genes); item.genes.postln};
			"-> Evolved to: %".format(evolved).postln;

			Pspawner({|sp|
				sp.wait(0.25);
				"Playing the elite...".postln;
				sp.seq(Pbind(\midinote, Pseq(eliteSequence.flatten,1), \dur, 0.15));
				sp.wait(0.25);
				"Playing the target...".postln;
				sp.seq(Pbind(\midinote, Pseq(evolved,1), \dur, 0.15));
			}).play

			//Pbind(\midinote, Pseq(evolved), \dur, 0.15).play
		}};
		0.1.wait;
	}
}).start
)

t.pause;
t.resume; //Wanna keep on going?
```

```js
// ---------------------------- DNAText -------------------------------//

// How DNAText works:
(
var target = "hello";
var size = target.size;
a = DNAText(size);
b = DNAText(size);
a.calcFitness("hello");
"Genes: %".format(a.genes).postln;
"Gene: size %".format(a.geneSize).postln;
"Fitness: %".format(a.fitness).postln;
x = a.copulate(b);
"Next breed %".format(x.genes).postln;
""
)
```

// ---------------------------- Evolve a word or phrase -------------------------------//
(
var target = "hello";
var mutationRate = 0.25;
var population = 100;
var creatures;
var matingPool = List.new();

creatures = population.collect{ DNAText(target.size) };

t = Task({
	var generation = 0;
	creatures.do{|dna, i| "Initial state %: %".format( i+1, dna.genes).postln };
	inf.do{
		generation = generation + 1;
		creatures.do{|dna| dna.calcFitness(target)};
		population.do{ |i|
			var nth = (creatures[i].fitness * 100); // A multiplier to loop based on fitness. Gets us more of the best ones.
			nth.do{
				matingPool.add(creatures[i]) // The fittest gets in the pool
			}
		};
		population.do{ |i|
			var pickA = rrand(0, matingPool.size-1);
			var pickB = rrand(0, matingPool.size-1);
			var male = matingPool.at(pickA);
			var female = matingPool.at(pickB);
			var child = male.copulate(female);
			child.mutate(mutationRate);
			creatures[i] = child; //Refresh the creature population
		};
		"Generation: % ".format(generation).postln;
		population.do{|i| if (creatures[i].getGenes == target) {
			var evolved = creatures[i].getGenes;
			t.stop;
			"-> Evolved to: %".format(evolved).postln;
//			Pbind(\midinote, Pseq(evolved), \dur, 0.15).play
		}};
		0.1.wait;
	}
}).start
)

t.stop
t.resume //Wanna keep on going?
