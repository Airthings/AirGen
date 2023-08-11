# AirGen Text Generator

Welcome to AirGen! A general-purpose text generator made with love by the
[Airthings Team](https://github.com/Airthings).

## What is it?

AirGen reads any structured model from either a JSON or Yaml file and feeds it to a FreeMarker template
of your choosing, the resulting files are stored in an output directory using the same structure as in
the original template directory.

AirGen automatically scans the template directory for any `*.ftl` files, but ignores files or directories
that start with an underscore `_` character. This way you're able to prepare include files and directories
that can be used by many templates.

## Usage

To see the usage: `./gradlew run --args="-h"`

## A note about `--base` and `--template`

A base directory is merely a directory where all other templates reside. When you set the base directory,
it becomes the root from which other templates and files are loaded. It's not possible to load files from
a location which doesn't reside in the base (root) directory.

Example of a directory structure:

```
templates/
  l10n-poeditor/                 <-- base directory
    android/
      en/                        <-- template "android/en"
        src/main/res/values/
          strings.xml.ftl
      fr/                        <-- template "android/fr"
        src/main/res/values/
          strings.xml.ftl
      de/                        <-- template "android/de"
        src/main/res/values/
          strings.xml.ftl

    ios/

```

Then you can run AirGen 3 times in order to generate localization files for the 3 templates:

```shell script
$ ./gradlew run --args="-o app -b templates/l10n-poeditor -t android/en -m strings-en.json"
$ ./gradlew run --args="-o app -b templates/l10n-poeditor -t android/fr -m strings-fr.json"
$ ./gradlew run --args="-o app -b templates/l10n-poeditor -t android/de -m strings-de.json"
```

The structure of the output directory mimics that of the template directory, NOT the base directory. So
`app/` directory would eventually contain the directories `src/main/res/values`, `src/main/res/values-fr`,
and `src/main/res/values-de`.

## Use of the model in FreeMarker templates

The JSON or Yaml file is read once and made available to the templates as a top-level parameter named `input`.

For example, if you have the following JSON structure:

```json
{
  "user": {
    "name": "John Doe",
    "email": "john@example.com"
  },
  "token": "XYZ0123456789",
  "children": [
    "Sarah",
    "Winston",
    "Debbie"
  ]
}
```

In the FreeMarker template files (`*.ftl`) you can simply access the different values using the following notations:

```injectedfreemarker
Hello ${input.user.name}.

Your email is ${input.user.email}, and your token is ${input.user.token}.
<#if (input.children?has_content)>

Your children:
  <#list input.children as child>
  ${child}
  </#list>
</#if>
```

Easy enough :)

You can read more about other built-in accessors like the `?has_content` on the
[FreeMarker website](https://freemarker.apache.org/docs/ref_builtins.html).

## Accessing Java methods

Most Java methods can be accessed on resolved variables right from FreeMarker template files, but if there exists a
builtin accessor to do the same job, always use the latter.

FreeMarker mostly treats the `.something` as a key/call on a `Map` so it may sometimes throw an Exception if you
try to access, say, `variable.trim()`. You should instead use `variable?trim` for that.

## Additional AirGen functionality

There are few helper methods accessible through the `air` variable. The list may grow in the future as our needs
require adding a custom logic to do some nifty processing, so do keep the following list updated:

### Number formatting

These methods accept either a `Number` or a `String`, and return it after processing based on the method being called.
The methods are accessible through the `air.number` variable.

#### Converting a number to hexadecimal, with or without a leading "0x" prefix

`fun toHex(number: <Number|String>, withPrefix: Boolean, minimalDigits: Int?)`

Where:

* `number`: The number to convert to a hexadecimal notation.
* `withPrefix`: Whether to prefix returned number with "0x", or not.
* `minimalDigits`: Minimal number of digits to zero-fill the returned number.

#### Ensuring a minimal number of digits for a number

`fun zeroFill(number: <Number|String>, minimalDigits: Int)`

Where:

* `number`: The number to zero-fill.
* `minimalDigits`: Minimal number of digits to zero-fill the returned number.

### HTML manipulation

These methods accept a `String` and return it after processing for certain manipulation for HTML.
The methods are accessible through the `air.html` variable.

#### Escaping `"` and `'` with their HTML counterparts: `&#34;` and `&#39;`.

`fun escapeQuotes(string: String, double: Boolean, single: Boolean): String`

Where:

* `string`: The string to escape.
* `double`: Whether to convert `"` to `&#34;`.
* `single`: Whether to convert `'` to `&#39;`.

Example use case: escaping quotes for Android `strings.xml` files.

```injectedfreemarker
<string>"${air.html.escapeQuotes(translation, true, true)}"</string>
```

#### Escaping HTML characters: `<`, `>`, and `&`

`fun escapeHtml(string: String): String`

Where:

* `string`: The string to escape.

Example use case: escaping for Android `strings.xml` or Markdown files.

```injectedfreemarker
<string>"${air.html.escapeHtml(translation)}"</string>
```

### Other text manipulation

All other methods dealing with text manipulation reside here, and are accessible through the `air.text`variable.

#### Escaping `"` and `'` with `\"` and `\'`.

`fun escapeQuotes(string: String, double: Boolean, single: Boolean): String`

Where:

* `string`: The string to escape.
* `double`: Whether to convert `"` to `\"`.
* `single`: Whether to convert `'` to `\'`.

Example use case: escaping quotes for iOS `Localizable.strings` files.

```injectedfreemarker
<string>"${air.text.escapeQuotes(translation, true, true)}"</string>
```

#### Turning a multi-line string (having `\r` or `\n`) to a one liner

`fun oneLiner(string: String, trimLines: Boolean): String`

Where:

* `string`: The string to convert.
* `oneLiner`: Whether to convert new line characters to `\n` and `\r`, yielding a "one liner" result.

Example use case: eliminating stray spaces for Android `strings.xml` files.

```injectedfreemarker
<string>"${air.text.oneLiner(translation, true)}"</string>
```

### Combining multiple calls

You can of course combine multiple calls to these methods, and FreeMarker has a nifty assignment command to break
things up nicely:

```injectedfreemarker
<#assign translation = input.l10n.definition?trim>
<#assign translation = air.text.oneLiner(translation, true)>
<#assign translation = air.html.escapeHtml(translation)>
<#assign translation = air.html.escapeQuotes(translation, true, true)>
<string>"${translation}"</string>
```

## Future plans

* Passing properties on the command line, accessible in FreeMarker templates through the `prop` variable.
* Read run configuration from a file.
