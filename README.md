# TestAntivirus
Test task to detect EICAR virus apps

* Since usage of libraries is discouraged then simple calls and returns will be used where I would have used RxJava primitives (Single, Completable, Flowable, etc.)

* Repository module is created to display usage of CleanArchitecture - in the real app it would have synced storage and remote modules and also contained some logic for offline\online states of the app. In this example repo classes are nothing more than simple wraps around storage classes.

* For simplicity I use SharedPreference to store virus signatures and found threats. Of cause it woulnd't be a good solution for a good app, it was done this way just to demonstrate that storage is needed in AntiVirus app. Yes, to simplify thing I used GSON library here, I hope it's OK. Also, I haven't put much effort into thread-safety of storage though it is very important for real AntiVirus which would have several different threads scanning the device.
* Of cause work with threat signatures must me heavily optimized in real AntiVirus, so that search would be as fast as possible. But in this test app I skipped that part.
* Though I made storage of found threats, that was done just for demonstartion purposes. For simplicity, I never implemented synchronization of previously and newly found threats, also data can come from overllapping full and partial scans. That is a huge task by itself, as several different threats may provide info about threats. I have implemented such syncronization when I worked at previous company.
* The way I implemented file scanning is oversimplified, but it's a test task after all :))
* Of course, there are lots of things to improve in architecture and test coverage. But this task was really big so I tried to save time on this tech debt. In normal case, I would have covered by unit tests all classes outside of 'app' module.

* The task about saving a secret string: in my opinion the best case is when the secret is not stored in the APK at all. If secret is stored on server that should be better. Besides, it's much better to have different secrets for each user, instead of common secret for all app installations.
