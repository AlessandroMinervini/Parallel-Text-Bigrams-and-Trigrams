#include <iostream>
#include <string>
#include <stdlib.h>
#include <unordered_map>
#include <chrono>
#include <algorithm>
#include <vector>
#include <thread>
#include <math.h> 

#include "parallel_thread.h"

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

	auto s = chrono::high_resolution_clock::now();

	char* text = readTextFromFile();

	string txt = text;

	txt.erase(remove(txt.begin(), txt.end(), '\n'), txt.end());

	int len = txt.length();
	unsigned int nThread = 2;		//number of threads

	parallel_thread *threads[nThread];		//array of threads

	vector<unordered_map<string, int> > maps;		//vector of maps

	unordered_map<string, int> finalMap;

	int n = 3;		//dimension of n-grams 

	if(n == 2){		//bigrams computation
		threads[0] = new parallel_thread(0, n, 0, ceil(len/nThread) - 1, txt);		//first iteration
		for(int i =1; i <nThread; i++){
			threads[i] = new parallel_thread(i, n, ceil((i*len)/nThread) - 1, ceil((i+1)*len/nThread) - 1, txt);
	 	}
	}
	else{
		if(n == 3){		//trigrams computation
			threads[0] = new parallel_thread(0, n, 0, ceil(len/nThread), txt);		//first iteration
			for(int i =1; i <nThread; i++){
				threads[i] = new parallel_thread(i, n, ceil((i*len)/nThread) - 1, ceil((i+1)*len/nThread) - 1, txt);
		 	}
		}
	}

	for(int i =0; i <nThread; i++){		//start all threads
		threads[i]->start();
 	}

 	for(int i =0; i <nThread; i++){		//join all threads and push in maps[] all the maps
		threads[i]->join();
		maps.push_back(threads[i]->getMap());
 	}

 	finalMap = maps[0];
 	for(int i =1; i < maps.size(); i++){		//iterate through maps and print pairs
 		finalMap = hashMerge(maps[i], finalMap);
 	}

 	for (auto& x: finalMap){
    		cout << x.first << ": " << x.second << endl;
		}

	auto finish = chrono::high_resolution_clock::now();
	chrono::duration<double> elapsed = finish - s;

	cout << "Elapsed time: " << elapsed.count() << " s\n";

	return 0;
}

