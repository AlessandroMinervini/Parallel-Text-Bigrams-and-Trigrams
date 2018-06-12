#include <thread>
using namespace std;

class parallel_thread : Thread { 
public: 
	void* run() { 
		cout<<'Hello!';

	} 

}; 


