#include <pthread.h>
#include <string>
#include <unordered_map>

using namespace std;

class Thread{

public:

	int *ptr;
	virtual int *run() = 0;

	pthread_t self();

	~Thread(){
		if(running && !detached)
			pthread_detach(tid);
		
		if(running)
			pthread_cancel(tid);
	}

	void join(){
		pthread_join(tid, (void**)&ptr);
	}

	int start(){

		int result = pthread_create(&tid, NULL, runThread, this);

		if(result == 0)
			running = true;

		return result;
	}

	static void* runThread(void* arg){
		return (static_cast<Thread*>(arg))->run();
	}

private:

	pthread_t tid;
	bool running, detached;
};