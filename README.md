# AWS Amplify: Extended

An extensible platform for adding new <u>categories</u> of functionality to [AWS Amplify for Android](https://github.com/aws-amplify/amplify-android). Note that a functionality category is different from a plugin implementation.

## Categories vs. Plugins

> Amplify exposes different *categories* of functionality: Auth, API, DataStore, Predictions, Storage, Analytics, etc. Categories address common use cases that a developer encounters while building an app.
>
> AWS provides a default set of plugins that can be used to fulfill these categories. The plugins fulfill category *behaviors* by making use of AWS backend services. A behavior is any action that can be performed in a category. For example, `post` is a behavior of the API category.
>
> One of the core design principles of Amplify is that you can add your own plugins. Your plugin might fulfill a category by using some AWS service other than the default. But, it also need not interact with any AWS service, at all.  
>
> -- Jameson Williams, [Building a Custom Android Plugin for AWS' Amplify Framework](https://medium.com/@jamesonwilliams/building-a-custom-android-plugin-for-aws-amplify-framework-821d50f0128f)

## Do I Need a Category?

If you'd like to create a plugin that falls into one of the following categories:

* **[Authentication](https://docs.amplify.aws/lib/auth/getting-started/q/platform/android)**

- **[Storage](https://docs.amplify.aws/lib/storage/getting-started/q/platform/android)**
- **[DataStore](https://docs.amplify.aws/lib/datastore/getting-started/q/platform/android)**
- **[API
  (GraphQL)](https://docs.amplify.aws/lib/graphqlapi/getting-started/q/platform/android)**
- **[API
  (REST)](https://docs.amplify.aws/lib/restapi/getting-started/q/platform/android)**
- **[Analytics](https://docs.amplify.aws/lib/analytics/getting-started/q/platform/android)**
- **[Predictions](https://docs.amplify.aws/lib/predictions/getting-started/q/platform/android)**

then Amplify Extended may not be for you. Consider [Building a Custom Android Plugin for AWS’ Amplify Framework](https://medium.com/@jamesonwilliams/building-a-custom-android-plugin-for-aws-amplify-framework-821d50f0128f).

This project is specifically aimed to allow *new* functionality categories.

Consider the video streaming use case. [Amplify Video](https://github.com/awslabs/amplify-video) is a category and plugin for Amplify CLI not maintained by the AWS Amplify team. Integrating Amplify Video into your Android app can be accomplished with the [Video component and plugin](#broken-link) for Amplify Extended.

## Platform Support

The Amplify Framework supports Android API level 16 (Android 4.1) and above.



## Using Amplify Extended From Your App

1. Download the latest release: [extended-release.aar](https://github.com/alextyner/amplify-android-extended/raw/master/release/extended-release.aar)
2. Place the downloaded archive in the `app/libs` folder of your Android Studio project.
3. Ammend your `app` module's `build.gradle` dependencies section:

```groovy
dependencies {
    implementation files('libs/extended-release.aar')
    
  	// Amplify Extended plugin dependencies
    // ... and other dependencies
}
```

4. Because Amplify Android uses Java 8 features, add a `compileOptions`
   block inside your app's `build.gradle` as below:

```gradle
android {
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}
```

5. Run `gradle sync` and continue to the example section.



## Examples

Once you've configured Amplify Extended, getting started is similar to [getting started with Amplify Android](https://docs.amplify.aws/start/getting-started/setup/q/integration/android#create-a-new-android-application).

We'll assume that you've already initialized (or will initialize) your Amplify project and backend resources with Amplify CLI.

### Adding a new category

Let's add a *Video* category. Adding a non-standard Amplify functionality category is easy. 

```java
AmplifyExtended.addCategory(new VideoCategory());
```

### Registering a category plugin

We'll add an *AWS Video* plugin. What do we need?

1. To add a plugin, we'll need that category from before. It can be accessed via `AmplifyExtended.category()` using the identifier defined within the category's implementation. In this case, it's `video`.

2. We'll also need an instance of the plugin, `AWSVideoPlugin` in this case.

3. Amplify Extended plugins get their own configuration file, typically. Video uses `videoconfiguration.json` (in the `app/src/main/res/raw` folder), so we provide that file's identifier.

4. And finally, AmplifyExtended needs a fresh configuration to populate. Here, it's a `VideoCategoryConfiguration`.

```java
try {
  
    // Add AWS Video plugin
    AmplifyExtended.addPlugin(AmplifyExtended.category("video"), new AWSVideoPlugin(), "videoconfiguration", new VideoCategoryConfiguration());
  
    // Configure all Amplify Extended plugins
    AmplifyExtended.configure(getApplicationContext());
  
} catch (AmplifyException initErr) {
    Log.d("AMPEXT", "Amplify Extended couldn't be configured.", initErr);
}
```

5. Once all the Amplify Extended categories and plugins we want are added, configure them and initialize their state with `AmplifyExtended.configure()`.

### Using a category plugin

1. Retrieving that category that we added earlier is easy enough. Use `AmplifyExtended.category()` and provide its identifier.

```java
VideoCategory video = AmplifyExtended.category("video");
```

2. Use the functionality defined by the category. That's it.

```java
String uri = video.getLiveResource("mytestlivestream").getEgressPoint(EgressType.MEDIASTORE);
```



## License

This library is licensed under the [Apache 2.0 License](./LICENSE).

## Report a Bug

We appreciate your feedback -- comments, questions, and bug reports. Please
[submit a GitHub issue](#broken-link),
and we'll get back to you.

## Contribute to the Project

Please see the [Contributing Guidelines](./CONTRIBUTING.md).
