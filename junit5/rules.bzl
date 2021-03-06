load("@rules_jvm_external//:defs.bzl", "artifact")

# For more information see
# - https://github.com/bmuschko/bazel-examples/blob/master/java/junit5-test/BUILD
# - https://github.com/salesforce/bazel-maven-proxy/tree/master/tools/junit5
# - https://github.com/junit-team/junit5-samples/tree/master/junit5-jupiter-starter-bazel
def junit5_test(name, srcs, test_package, resources = [], deps = [], runtime_deps = [], **kwargs):
    """JUnit runner macro"""
    FILTER_KWARGS = [
        "main_class",
        "use_testrunner",
        "args",
    ]

    for arg in FILTER_KWARGS:
        if arg in kwargs.keys():
            kwargs.pop(arg)

    junit_console_args = []
    if test_package:
        junit_console_args += ["--select-package", test_package]
    else:
        fail("must specify 'test_package'")

    native.java_test(
        name = name,
        srcs = srcs,
        use_testrunner = False,
        main_class = "org.junit.platform.console.ConsoleLauncher",
        args = junit_console_args,
        deps = deps + [
            artifact("org.junit.jupiter:junit-jupiter-api"),
            artifact("org.junit.jupiter:junit-jupiter-params"),
            artifact("org.junit.jupiter:junit-jupiter-engine"),
            artifact("org.hamcrest:hamcrest-library"),
            artifact("org.hamcrest:hamcrest-core"),
            artifact("org.hamcrest:hamcrest"),
            artifact("org.mockito:mockito-core"),
        ],
        visibility = ["//java:__subpackages__"],
        resources = resources,
        runtime_deps = runtime_deps + [
            artifact("org.junit.platform:junit-platform-console"),
        ],
        **kwargs
    )
