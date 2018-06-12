#include <thread>
#include <unordered_map>
#include <string>

#include "Thread.h"

using namespace std;

class parallel_thread: public Thread{

private:
	string fileString;

public:
	int n, begin, stop, id;

	unordered_map<string, int> map;

	parallel_thread(int id, int n, int begin, int stop, string fileString){
		this->id = id;
		this->n = n;
		this->begin = begin;
		this->stop = stop;
		this->fileString = fileString;
    }

    int *run(){	 

		unordered_map<string, int> map;

		//string fileStr = this->fileString;

		//fileStr.erase(remove(fileStr.begin(), fileStr.end(), '\n'), fileStr.end());


		for (int i = this->begin + this->n - 1; i <= this->stop; i++){
			string key = "";

			for(int j = this->n - 1; j >= 0; j--){
				key = key + this->fileString[i-j];
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

		this->map = map;

		return 0;
	}

    unordered_map<string, int> getMap(){
    	return this->map;
    }

};

