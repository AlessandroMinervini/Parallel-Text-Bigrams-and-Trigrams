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

	char blackList[] = "~`!@#$%^&*()_-+=|][{}-':;?/>.<,òàèì°ç§é ";

	for (int i = 0; i < strlen(blackList); i++){
		removeChar(buffer, blackList[i]);
	}

	fclose (pFile);
	//cout<< buffer; 
	return buffer;
}

unordered_map<string, int> computeNgrams(int n, char* fileString){

	unordered_map<string, int> map;

	//const basic_string<char> fileStr = fileString;
	string fileStr = fileString;
	//cout<<fileString;
	//char copy [fileStr.length()]; 

	for (int i = n-1; i < fileStr.length(); i++){
		string key = "";

		for(int j=n-1; j>=0; j--){
			key = key + fileStr[i-j];
		}

		//cout<<"-"<<key[0]<<key[1];

		//for (int j = 0; j < n; j++){
		//	char c = fileStr[i+j];
		//	key.append(&c);
		//	cout<<key;
		//}
		//cout<<c1<<c2;

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

	return map;

}

int main(int argc, char const *argv[]){

	char* text = readTextFromFile();

	std::unordered_map<std::string, int> map = computeNgrams(3, text);
  	
	for (auto& x: map)
    	std::cout << x.first << ": " << x.second << std::endl;

	return 0;
}




