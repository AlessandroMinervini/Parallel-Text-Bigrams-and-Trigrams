#include <iostream>
#include <string>
#include <stdlib.h>
#include <unordered_map>
#include <chrono>
#include <algorithm>
#include <vector>
#include <thread>
#include <math.h>
#include <sys/time.h>
#include <time.h>

#include "Parallel_thread.h"

using namespace std;

void removeChar(char* s, char charToRemove){
	char* copy = s;
    char* temp = s;

    while (*copy){
    	if (*copy != charToRemove)
        	*temp++ = *copy;
        copy++;
    }

    *temp = 0;
}

char* readTextFromFile() {

	FILE * pFile;
 	long size;
 	char * buffer;
 	size_t result;

	pFile = fopen ( "text.txt" , "r" );
	if (pFile==NULL) {
		fputs ("File not found",stderr); exit (1);
	}

	//file size:
	fseek (pFile , 0 , SEEK_END);
	size = ftell (pFile);
	rewind (pFile);

	//allocate memory:
	buffer = (char*) malloc (sizeof(char) * size);
	if (buffer == NULL) {
		fputs ("Memory error",stderr); exit (2);
	}

	//copy the file into the buffer:
	result = fread (buffer,1,size,pFile);
	if (result != size) {
		fputs ("Reading error",stderr); exit (3);
	}

	for (int i = 0; i < size; ++i){		//makes the text lower
		char c = tolower(buffer[i]);
		buffer[i] = c;
	}

	char blackList[] = "~`!@#$%^&*()_-+=|][{}-':;?/>.<,òàèì°ç§é ";		//list of char to be removed from text

	for (int i = 0; i < strlen(blackList); i++){
		removeChar(buffer, blackList[i]);
	}

	fclose (pFile);
	return buffer;
}

unordered_map<string, int> hashMerge(unordered_map<string, int> map, unordered_map<string, int> finalMap) {

    for (auto& x: map){    
    	int newValue = x.second;		//value of x
    	int existingValue = finalMap[x.first];

    	if(existingValue != 0){
    		newValue = newValue + existingValue;
    	}

        finalMap[x.first] = newValue;
    }
    return finalMap;
}

int main(int argc, char const *argv[]){
	
	char* text = readTextFromFile();

	string txt = text;

	struct timeval start, end;
	gettimeofday(&start, NULL);

	int len = txt.length();

	unsigned int nThread = 2;		//number of threads
	int n = 2;		//dimension of n-grams 

	Parallel_thread *threads[nThread];		//array of threads

	vector<unordered_map<string, int> > maps;		//vector of maps

	unordered_map<string, int> finalMap;

	int k = floor(len/nThread); 	//step of text dimension


	for(int i =0; i <nThread; i++){
	 	threads[i] = new Parallel_thread(i, n, (k * i), (i + 1) * k + ((n - 1) -1), txt);	//create threads
	 	threads[i]->start();	//start all threads
	}

 	for(int i =0; i <nThread; i++){		//join all threads and push in maps[] all the maps
		threads[i]->join();
		maps.push_back(threads[i]->getMap());
 	}

 	finalMap = maps[0];
 	
 	for(int i =1; i < maps.size(); i++){		//iterate through maps and print pairs
 		finalMap = hashMerge(maps[i], finalMap);
 	}

  	gettimeofday(&end, NULL);

 	for (auto& x: finalMap){
    	cout << x.first << ": " << x.second << endl;
	}

  	string elapsed_time = to_string( ((end.tv_sec - start.tv_sec) * 1000.0 + ( end.tv_usec - start.tv_usec) / 1000.0) / 1.e3 );

  	cout << elapsed_time << endl;

	return 0;
}

