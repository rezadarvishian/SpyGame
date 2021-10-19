# Simple Spy Game: Source Code


[🚨Published version🚨](https://cafebazaar.ir/app/da.reza.spy)



This repository contains a detailed game app that implements simple game using Koin, Room, Coroutines, Flow,Channel , Navigation Component , Retrofit , Services and unit Test
<p align="center">
  <img src="https://user-images.githubusercontent.com/55354691/137774572-35149482-17cb-49c3-be9c-2b415d5d90ea.jpg" width="250">
  <img src="https://user-images.githubusercontent.com/55354691/137774665-ca90e7ca-2fa0-4920-8eb2-f23178cf40e9.jpg" width="250">
  <img src="https://user-images.githubusercontent.com/55354691/137774813-d3f8b9bb-343c-461b-9306-2c42dad9c5b4.jpg" width="250">
</p>




#### The app has using api:
1. **Pixabay**: Free Pixaby Api used to search For Images.[🚨Website](https://pixabay.com)
2. **VazheYab**: Paid Dictionary Api used to search For Synonymous words and translate Persian To English.[🚨Website](https://www.vajehyab.com)
3. **Firebase**: firebase api used to Crashlytics , Analytics , in-AppMessaging and CloudMessaging.[🚨Website](https://www.firebase.google.com)
```
Note : For Using Firebase futures add your google-services.json
```

### For Using this Source Code Add this line to your gradle.properties file

```
API_KEY ="Your Pixabay APi Key"
TAPSELL_KEY = "Your Tapsell Key For Using Ad Network"
NATIVE_AD_ZONE_ID = "Your tapsell ad Zone Id"
VAZHEYAB_TOKEN = "Your VazheYab Api Key"

```

#### The app has following packages:
1. **data**: It contains all the data accessing and manipulating components.
2. **di**: Dependency providing classes using Koin.
3. **ui**: View classes along with their corresponding ViewModel.
4. **utils**: Utility classes.
3. **ViewModel**: all viewModel Classes.





## License

Copyright 2021 , Reza Darvishian

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with the   License.
You may obtain a copy of the License in the LICENSE file, or at:

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS   IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
