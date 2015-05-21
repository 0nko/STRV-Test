Android Coding Guidelines
=========================

Android Coding Guidelines is a set of rules for writing and maintaining the Android code. It's not official guidelines. It's just rules for Android programmers at STRV.

The point of having coding style guidelines is to have a common vocabulary of coding, so people can concentrate on what you're saying, rather than on how you're saying it. We present global style rules here so people know the vocabulary. But local style is also important. If code you add to a a file looks drastically different from the existing code around it, it throws readers out of their rhythm when they go to read it. Try to avoid this.


Development Environment
=======================

We use Android Studio as a primary IDE. We use [Gradle](http://www.gradle.org/) for building applications and [Maven](http://maven.org) for describing dependencies.


Project Structure
-----------------

Each Android project has the following base structure (directories are marked by square braces):

- [extras]
- [extras]/[keystore]
- [extras]/[keystore]/example.keystore
- [extras]/[keystore]/example.properties
- [gradle]
- [gradle]/[wrapper]
- [mobile]
- [mobile]/[libs]
- [mobile]/[src]
- [mobile]/[src]/[main]
- [mobile]/[src]/[main]/[assets]
- [mobile]/[src]/[main]/[java]
- [mobile]/[src]/[main]/[res]
- [mobile]/[src]/[main]/AndroidManifest.xml
- [mobile]/build.gradle
- [mobile]/proguard-rules.pro
- [vendors]
- .gitignore
- build.gradle
- gradle.properties
- gradlew
- gradlew.bat
- README.md
- settings.gradle

Main module for mobile application should be named as "mobile". Module for Wear app should be named "wear". Module for shared classes and resources should be named "base". All dependencies should be described in Gradle script. If the library is available in Maven Central, use it. If not, place JAR/AAR file into _libs_ directory. If there is no JAR/AAR file, put dependency as module into _vendors_ directory. See [Gradle template](https://github.com/petrnohejl/Android-Templates-And-Utilities/blob/master/Base/mobile/build.gradle).

Always use [Gradle Wrapper](http://www.gradle.org/docs/current/userguide/gradle_wrapper.html). Wrapper should be submitted to version control system.

If you need to store some special resources (documentation, certificates, special utilities), put it into _extras_ directory. Signing keystore file should be stored in _/extras/keystore_ directory. Keystore alias and password should be stored in property file, which is automatically read by Gradle script.

_AndroidManifest.xml_ shouldn't contain _versionCode_, _versionName_, _minSdkVersion_, _targetSdkVersion_ properties. Those are defined via Gradle script. See [Manifest template](https://github.com/petrnohejl/Android-Templates-And-Utilities/blob/master/Base/mobile/src/main/AndroidManifest.xml).

Each project must have _README.md_ file in Markdown format where should be described how to build project, which dependencies are used and who developed it. See [README template](https://github.com/petrnohejl/Android-Templates-And-Utilities/blob/master/Base/README.md).

Use a [template for creating basic skelet](https://github.com/petrnohejl/Android-Templates-And-Utilities/tree/master/Base) of the Android project.


Versioning
----------

We use [Semantic Versioning](http://semver.org/). Version name should be in this format: `MAJOR.MINOR.PATCH`. It is recommended to calculate version code this way: `VERSION_MAJOR*10000000 + VERSION_MINOR*100000 + VERSION_PATCH*1000 + VERSION_BUILD`. See [Gradle template](https://github.com/petrnohejl/Android-Templates-And-Utilities/blob/master/Base/mobile/build.gradle).


Version Control System
----------------------

We use [Git](http://git-scm.com/) in most cases, sometimes [Mercurial](http://mercurial.selenic.com/). You can use [TortoiseGit](http://code.google.com/p/tortoisegit/) or [TortoiseHg](http://tortoisehg.bitbucket.org/) client on Windows.

Each project must contain _.gitignore_ or _.hgignore_. Use a [template for .gitignore](https://github.com/petrnohejl/Android-Templates-And-Utilities/blob/master/Base/.gitignore). Write commit messages in English, in present time. Message should start with capital letter. Message should describe well the changes in the code. For example "Fix NullPointerException in ProductListAdapter" is much more better message than "fixed bug". The latter is wrong because it is in past tense, lower-case and not so descriptive. Release commit must be tagged with a version name. Use this convention for tags: "1.0.0", don't use this one: "v1.0.0". Main branche should be named as "master". Development branche should be named as "dev".

Always commit only working source code which is possible to built. Never commit temporary files, binaries etc.

See [A successful Git branching model](http://nvie.com/posts/a-successful-git-branching-model/) for more info about Git branches.


Rules for Signing APK
---------------------

Each application should be signed by unique keystore certificate, which was created especially and only for this project. Keystore can be submitted to private version control system. Why should you use a unique certificate for each app? Because certificate of the published app cannot be changed. If somebody want to transfer the application, he must also provide the certificate, because it is the essential part of the Android application.

A new keystore should have a suitable alias in lowercase. E.g. project "The Game" could have alias "thegame". Keystore also should have secure password. You can use [Generista password generator](http://generista.com/). Keystore file name should be in this format: `<alias>.keystore`, e.g. "thegame.keystore". Property file with alias and password should be in this format: `<alias>.properties`, e.g. "thegame.properties". These files should be stored in _/extras/keystore_ directory.

Example of property file:

	keystore.store.password=mypassword
	keystore.key.alias=myalias
	keystore.key.password=mypassword


Source Code
===========

Always write a clean code. Use consistent conventions. If you're editing code, take a few minutes to look at the code around you and determine its style. If they use spaces around their if clauses, you should too etc. Write comments in English.


Indentation
-----------

Indent the code using tabs (size 4) and 2 empty lines between methods. Use standard [K&R brace style](http://en.wikipedia.org/wiki/Indent_style#Variant:_1TBS) or eventually [Allman brace style](http://en.wikipedia.org/wiki/Indent_style#Allman_style).


Packages
--------

Package name should be in lower case. It is good to use a domain name for base package name. Example: "com.myapp.android".

Project should be structured into packages. Packages should be also in lower case and singular form, e.g. "com.myapp.android.fragment". Don't use "com.myapp.android.fragments". We usually use these packages:

- activity
- adapter
- alarm
- broadcast
- client
- client/parser
- client/request
- client/response
- client/ssl
- database
- database/dao
- database/data
- database/model
- database/query
- dialog
- entity
- fragment
- gcm
- geolocation
- listener
- notification
- receiver
- service
- synchro
- task
- utility
- view


Classes
-------

Class names for Activities, Fragments, Adapters, Dialogs, Broadcasts, Listeners, Requests, Parsers, Queries, Entities etc. should be in this format: `<name><type>.java`. Examples: HelpActivity.java, MapFragment.java, AboutDialogFragment.java, ProductEntity.java.

If some activity or fragment has list and detail, the convention should be: `<name><List|Detail><type>.java`. Examples: ProductListActivity.java, ProductDetailActivity.java. Wrong example: ProductsActivity.java.

Listeners has usually this naming convention: `On<action>Listener.java` where `<action>` should describe some action (verb). Examples: OnRefreshViewListener.java, OnItemDeleteListener.java, OnLoadProductListListener.java.

Request and Parser classes should be named just as REST API methods. For example request for "product-list" method should be named: ProductListRequest.java.


Resources
---------

All colors, dimensions and strings should be always stored in XML files. It shouldn't be defined directly in layouts. There is one exception: you can use `0dp` directly in layout, e.g. for defining 0dp height in combination with `android:layout_weight`.

Drawable names should follow standard [naming conventions](http://petrnohejl.github.io/Android-Cheatsheet-For-Graphic-Designers/#naming-conventions). MDPI, HDPI, XHDPI and XXHDPI densities should be supported. XML drawables should be prefixed with the name of root element in XML. Selectors should be prefixed with "selector", e.g. "selector\_clickable\_item\_bg.xml". Shapes should be prefixed with "shape", e.g. "shape\_ab\_action\_basket\_quantity\_bg.xml".

Layout names should be in this format: `<type>_<name>.java`. Common prefixes are: "activity", "fragment", "dialog", "ab", "toolbar", "drawer", "placeholder". Examples: activity\_main.xml, fragment\_main.xml, fragment\_main\_item.xml, fragment\_main\_header.xml, fragment\_main\_footer.xml, dialog\_password\_update.xml, placeholder\_offline.xml, ab\_action\_basket.xml, drawer\_item.xml, search\_suggestion\_item.xml. For adapter layout use "fragment\_product\_list\_item.xml" rather than "adapter\_product\_list.xml".

Menu names should be in this format: `<type>_<name>.java`. Examples: menu\_main.xml, menu\_profile.xml.

Colors, dimens, strings should be in this format: `<type>_<name>`. Common prefixes are: "global", "ab", "toolbar", "drawer", "view", "placeholder", "activity", "fragment", "dialog", "notification". Organize resources in XML file in this order. Define global colors, font sizes and spacing metrics based on [4dp baseline grid](http://www.google.com/design/spec/layout/metrics-keylines.html). Use global [text appearance styles](http://www.google.com/design/spec/style/typography.html) for styling text views. See [theme template](https://github.com/petrnohejl/Android-Templates-And-Utilities/tree/master/Res-Theme/values) and [strings template](https://github.com/petrnohejl/Android-Templates-And-Utilities/tree/master/Res-Strings/values) for examples.

If you define some color or dimen for more screen sizes using qualifiers, it's recommended to add a comment `<!-- different for various screen sizes -->` next to the resource definition in XML file.

All resources related to preferences define in prefs.xml. See [preferences template](https://github.com/petrnohejl/Android-Templates-And-Utilities/blob/master/Src-Preferences/res/values/prefs.xml) or [PreferenceFragment template](https://github.com/petrnohejl/Android-Templates-And-Utilities/blob/master/Src-PreferenceFragment/res/values/prefs.xml).

Distinguish between theme and style. Theme name should be in this format: `Theme.<appname>.<light>`. Examples: Theme.Weather, Theme.TheGame.Light. Note that the dot stands for inheritance. Style name should be in this format: `<type>.<appname>.<element>.<inverse>`. Examples: Widget.Example.Toolbar, TextAppearance.Example.Title.Inverse. See [theme template](https://github.com/petrnohejl/Android-Templates-And-Utilities/blob/master/Res-Theme/values/).


Fields and variables
--------------------

Define constants and fields in standard places - at the top of the file. First constants, then fields. Follow the field naming conventions. Non-public, non-static field names start with _m_. Static field names start with _s_. Other fields start with a lower case letter. Public static final fields (constants) are _ALL\_CAPS\_WITH\_UNDERSCORES_.

Example:

	public class MyClass {
		public static final int SOME_CONSTANT = 42;
		public int publicField;
		private static MyClass sSingleton;
		int mPackagePrivate;
		private int mPrivate;
		protected int mProtected;
	}

Entities doesn't need to use _m_ prefix in field names.


Methods
-------

Write short methods. Methods should be kept small and focused. If a method exceeds 40 lines or so, think about whether it can be broken up without harming the structure of the program.

Write methods in this order: constructors, static methods, overridden methods, public methods, private methods, getters/setters. Group related methods together.

Method which load main data from API or database should be named: `loadData()`.

Method which pass some data to views should be named: `renderView()`.

Static method for creating a new Intent should be named `newIntent()`. Static method for creating a new Fragment should be named `newInstance()`.

Methods which show/hide some layout or dialog should be named: `show<name>()`. Examples: showContent(), showProgress(), showOffline(), showEmpty(), showActionBarProgress(), showLazyLoadingProgress(), showAboutDialog().

Methods which send some date to server should be named: `send<name>()`. Examples: sendOrderRequest(), sendLoginRequest().

Methods which setup some component should be named: `setup<name>()`. Examples: setupActionBar(), setupDrawer(), setupMap().

Methods which start a new Intent should be named: `start<name>()`. Examples: startCalendarActivity(), startProfileActivity().


Configuration
-------------

Each project should have a config file and an application class, both placed in the main package. Config class name should be in this format: `<appname>Config.java`. Application class name should be in this format: `<appname>Application.java`. Examples: TheGameConfig.java, TheGameApplication.java. See [config template](https://github.com/petrnohejl/Android-Templates-And-Utilities/tree/master/Src-Config) and [application template](https://github.com/petrnohejl/Android-Templates-And-Utilities/tree/master/Src-Application-Class).

You should use a [Logcat utility](https://github.com/petrnohejl/Android-Templates-And-Utilities/tree/master/Src-Logcat) and disable logs in production build.


Do and don't
============

- Follow [Google Design Guidelines](http://www.google.com/design/) and [Android Design Guidelines](https://developer.android.com/design/index.html).
- Target SDK should be set to the maximum API level.
- App should support both - landscape and portrait mode.
- Main application logic should be implemented in Fragments. Activity serves as a wrapper for Fragments. Use retained Fragments.
- Use public static method for creating a new instance of Fragment. Use public static method for creating a new Intent.
- Handle all possible states - content, progress, offline, empty etc.
- Don't use Service if it is not absolutely necessary. Better to use IntentService.
- Use view holder pattern in adapters.
- Clickable views should have a touch feedback.
- Don't use splash screen.
- Don't use progress dialogs.
- Watch out for overdraw.


Useful Resources
================

- [Android Asset Studio](http://android-ui-utils.googlecode.com/hg/asset-studio/dist/index.html) - Graphic asset generators
- [Android Cheatsheet for Graphic Designers](http://petrnohejl.github.io/Android-Cheatsheet-For-Graphic-Designers/) - Cheatsheet for graphic designers
- [Android Templates And Utilities](https://github.com/petrnohejl/Android-Templates-And-Utilities) - Collection of templates, source codes, utilities
- [Android Weekly](http://androidweekly.net/) - Newsletter for Android developers


Written by
==========

Petr Nohejl


License
=======

Copyright 2014 STRV
