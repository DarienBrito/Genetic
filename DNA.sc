/*

This classes were made by adapting code from
Daniel Shiffman's ""The Nature of Code""

Darien Brito - 2015

*/

DNA {
	var <>genes, <geneSize, <fitness;
	var min, max;

	*new { | geneSize, min = 36, max = 96 |
		^super.new.init(geneSize, min, max);
	}

	init { | geneSize_, min_, max_ |
		min = min_;
		max = max_;
		geneSize = geneSize_;
		genes = Array.rand(geneSize, min, max); // Testing range 2 octaves lower middle c, 3 octaves higher
	}

	calcFitness { |target|
		var score = 0;
		genes.do{|item, i| if(item == target[i]) {score = score + 1} };
		fitness = score / target.size;
		^fitness
	}

	copulate { | partner |
		var child = DNA(geneSize); // A new object
		var midpoint = rrand(0, geneSize); //Select middle point for mutation.
		genes.do {|gene, i| if(i > midpoint) { child.genes[i] = gene }
			{ child.genes[i] = partner.genes[i] }
		};
		^child
	}

	mutate {|rate|
		genes.size.do{ |i|
			if(rand(1.0) < rate) {
				genes[i] = rrand(min, max);
			}
		}
	}

	getGenes {
		^genes
	}

}

DNASmart : DNA { // Gets info from input target automatically
	var <target;

	*new { | target |
		^super.new.init(target)
	}

	init { |target_|
		var min = target.minItem;
		var max = target.maxItem;
		target = target_;
		geneSize = target.size;
		genes = Array.rand(geneSize, min, max); // Will search between found boundaries

	}

	calcFitness {
		var score = 0;
		genes.do{|item, i| if(item == target[i]) {score = score + 1} };
		fitness = score / target.size;
		^fitness
	}
}


DNAText : DNA {

	init { | geneSize_ |
		geneSize = geneSize_;
		genes = Array.rand(geneSize, 32, 128).asAscii; //All letters
	}

	copulate{ |partner|
		var child = DNAText(geneSize);
		var midpoint = rrand(0, geneSize);
		geneSize.do {|i|
			if(i > midpoint) { child.genes[i] = genes[i] }
			{ child.genes[i] = partner.genes[i] }
		};
		^child
	}

	mutate { |rate|
		genes.size.do{ |i|
			if(rand(1.0) < rate) {
				genes[i] = rrand(32, 128).asAscii;
			}
		}
	}

}


DNARythm : DNA { //This one is a bad idea.

	init { | geneSize_ |
		geneSize = geneSize_;
		genes = { [0.125,0.25,0.5,0.75].choose } ! geneSize; //Initial testing range [regular rythms]
	}

	copulate{ |partner|
		var child = DNARythm(geneSize);
		var midpoint = rrand(0, geneSize);
		geneSize.do {|i|
			if(i > midpoint) { child.genes[i] = genes[i] }
			{ child.genes[i] = partner.genes[i] }
		};
		^child
	}

	mutate { |rate|
		genes.size.do{ |i|
			if(rand(1.0) < rate) {
				genes[i] = [0.125,0.25,0.5,0.75].choose;
			}
		}
	}

}
