#include <iostream>
#include <string>
#include <stdlib.h>
#include <assert.h>
#include <unordered_map>
#include <chrono>
#include <thread>
#include <algorithm>
//#include "parallel_thread.h"
using namespace std;


void removeChar(char* s, char charToRemove){
	char* copy = s;  // an alias to iterate through s without moving s
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

	for (int i = 0; i < size; ++i){
		char c = tolower(buffer[i]);
		buffer[i] = c;
	}

	char blackList[] = "~`!@#$%^&*()_-+=|][{}-':;?/>.<,òàèì°ç§é ";

	for (int i = 0; i < strlen(blackList); i++){
		removeChar(buffer, blackList[i]);
	}

	fclose (pFile);
	return buffer;
}

unordered_map<string, int> computeNgrams(int n, char* fileString, unsigned int nThread, int start, int stop){

	unordered_map<string, int> map;

	string fileStr = fileString;

	fileStr.erase(remove(fileStr.begin(), fileStr.end(), '\n'), fileStr.end());


	for (int i = start + n-1; i < stop; i++){
		string key = "";

		for(int j=n-1; j>=0; j--){
			key = key + fileStr[i-j];
		}

		if(map.count(key) == 0){

				pair<string,int> pair (key,1);
                map.insert(pair);
            }
            else{

                if(map.count(key) >= 1){

                    map[key] += 1;
                }
            }
    }
    	for (auto& x: map){
    	cout << x.first << ": " << x.second << endl;
	}
	return map;
}

int main(int argc, char const *argv[]){

	auto start = chrono::high_resolution_clock::now();

	char* text = readTextFromFile();

	unsigned int nThread = thread::hardware_concurrency();

	thread threads[nThread];
	unordered_map<string, int> maps[nThread];

	int len = strlen(text);
	cout<<len;

	//unordered_map<string, int> map = computeNgrams(2, text);
	for(int i =0; i <nThread; i++){
		thread T(computeNgrams, 2 ,text, nThread, (i*len)/nThread, (i+1)*len/nThread);
		T.join();
	}



	auto finish = chrono::high_resolution_clock::now();
	chrono::duration<double> elapsed = finish - start;
	cout << "Elapsed time: " << elapsed.count() << " s\n";


	return 0;
}