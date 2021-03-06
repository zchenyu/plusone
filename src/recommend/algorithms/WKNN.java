package recommend.algorithms;

import java.util.*;

import recommend.util.WordIndex;

public class WKNN extends Algorithm {
	int K;
	
	List<HashMap<Integer,Double>> traindocs;
	double[] trainnorms;
	double[] itemAverages;

	
	public WKNN( int K ) {
		super( "WKNN-" + K );
		this.K = K;
	}

    public void train( List<HashMap<Integer,Double>> traindocs ) {
    	ItemAverage items = new ItemAverage();
    	items.train(traindocs);
    	itemAverages = items.predict(traindocs.get(0));
    	
    	this.traindocs = traindocs;
    	trainnorms = new double[traindocs.size()];
    	
    	for( int i = 0; i < trainnorms.length; i++ ) {
    		double norm2 = 0;
    		HashMap<Integer,Double> traindoc = traindocs.get( i );
    		
    		for( Integer word : traindoc.keySet() ) {
    			double score = traindoc.get( word );
    			norm2 += score*score;
    		}
    		
    		trainnorms[i] = Math.sqrt( norm2 );
    	}
    }

    public double[] predict( HashMap<Integer,Double> givenwords ) {
    	PriorityQueue<Pair> pq = new PriorityQueue<Pair>();
    	
    	double sumOfSims = 0.0;
    	
    	for( int i = 0; i < traindocs.size(); i++ ) {
    		double similarity = similarity( givenwords, i );
    		if( pq.size() < K ) {
    			pq.add( new Pair( i, similarity ) );
    		} else if( similarity > pq.peek().similarity ) {
    			pq.poll();
    			pq.add( new Pair( i, similarity ) );
    		}
    	}
    	
    	for (Iterator<Pair> i = pq.iterator(); i.hasNext();) {
    		sumOfSims += i.next().similarity;
    	}
    	double[] scores = new double[WordIndex.size()];
    	
    	while( !pq.isEmpty() ) {
    		Pair p = pq.poll();
    		HashMap<Integer,Double> traindoc = traindocs.get( p.doc );
    		
    		for( int word = 0; word < WordIndex.size(); word++ ) {
    			if (traindoc.get(word) != null)
    				scores[word] += (p.similarity*traindoc.get( word ))/sumOfSims;
    			 else
    				scores[word] += p.similarity*itemAverages[word]/sumOfSims;
    		}
    	}
    	
    	return scores;
    }
	
    private double similarity( HashMap<Integer,Double> words, int doc ) {
		int dp = 0;
		HashMap<Integer,Double> traindoc = traindocs.get( doc );

		for( int word : words.keySet() ) {
			if( traindoc.containsKey( word ) ) {
				dp += words.get( word )*traindoc.get( word );
			}
		}

		return dp/trainnorms[doc];
    }
    
	private static class Pair implements Comparable<Pair> {
		int doc;
		double similarity;
		
		public Pair( int doc, double similarity ) {
			this.doc = doc;
			this.similarity = similarity;
		}
		
		public int compareTo( Pair p ) {
			return similarity > p.similarity ? 1 : -1;
		}
	}
}
