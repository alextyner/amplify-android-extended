# Amplify for Android, Extended

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
* **[Storage](https://docs.amplify.aws/lib/storage/getting-started/q/platform/android)**

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



## Using Amplify Extended From Your App

1. In your Android Studio project, amend your `app` module's `build.gradle` dependencies section:

```groovy
dependencies {
    // ... other dependencies ...
  
    implementation 'com.amplifyframework:core:1.0.0'
    implementation 'com.amplifyframework:extended:0.1.7'
}
```

2. Because Amplify Android uses Java 8 features, add a `compileOptions`
   block inside your app's `build.gradle` as below:

```gradle
android {
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}
```

3. Sync your gradle project when prompted or run `gradle sync` and continue to the example section.



## Examples

Once you've configured Amplify Extended, getting started is similar to [getting started with Amplify Android](https://docs.amplify.aws/start/getting-started/setup/q/integration/android#create-a-new-android-application).

We'll assume that you've already initialized (or will initialize) your Amplify project and backend resources with Amplify CLI.

### Adding a new category

Using Amplify Extended categories is best illustrated with an example.

​	**See [Getting Started with Amplify Video for Android]().**

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

We appreciate your feedback -- comments, questions, and bug reports. Please [submit a GitHub issue](/issues),
and we'll get back to you.

## Contribute to the Project

Please see the [Contributing Guidelines](./CONTRIBUTING.md).
