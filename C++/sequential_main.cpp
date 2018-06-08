#include <iostream>
#include <string>
#include <stdlib.h>
#include <assert.h>
#include <unordered_map>

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
	if (pFile==NULL) {fputs ("File not found",stderr); exit (1);}

	//file size:
	fseek (pFile , 0 , SEEK_END);
	size = ftell (pFile);
	rewind (pFile);

	//allocate memory:
	buffer = (char*) malloc (sizeof(char) * size);
	if (buffer == NULL) {fputs ("Memory error",stderr); exit (2);}

	//copy the file into the buffer:
	result = fread (buffer,1,size,pFile);
	if (result != size) {fputs ("Reading error",stderr); exit (3);}

	for (int i = 0; i < size; ++i){
	char c = tolower(buffer[i]);
	buffer[i] = c;
	}

	char blackList[] = ",(.?')!:[; ]";

	for (int i = 0; i < strlen(blackList); i++){
		removeChar(buffer, blackList[i]);
	}

	fclose (pFile);

	return buffer;
}

unordered_map<string, int> computeNgrams(int n, char* fileString){

	unordered_map<string, int> map;

	const basic_string<char> fileStr = fileString;

	for (int i = 0; i < fileStr.length() - n + (n-1); i++){
		
		string key = "";

		for (int j = 0; j < n; j++){

			char c = fileStr[i+j];
			key.append(&c);
		}

		if(!map.count(key) && key.length() >= 2){

				pair<string,int> pair (key,1);
                map.insert(pair);

            }
            else{

                if(map.count(key) && key.length() > 2){

                    map[key] += 1;

                }
            }
    }

	return map;

}

int main(int argc, char const *argv[]){

	char* text = readTextFromFile();

	std::unordered_map<std::string, int> map = computeNgrams(2, text);
  	
	for (auto& x: map)
    	std::cout << x.first << ": " << x.second << std::endl;

	return 0;
}




