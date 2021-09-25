package org.archifacts.core.model;

import static org.archifacts.core.model.ApplicationBuilderTest.Classes.createAnonymousClass;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Named.named;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.archifacts.core.descriptor.ArtifactContainerDescriptor;
import org.archifacts.core.descriptor.BuildingBlockDescriptor;
import org.archifacts.core.descriptor.SourceBasedArtifactRelationshipDescriptor;
import org.archifacts.core.descriptor.TargetBasedArtifactRelationshipDescriptor;
import org.archifacts.core.model.ApplicationBuilderTest.Classes.ContainerType1_BuildingBlockType1;
import org.archifacts.core.model.ApplicationBuilderTest.Classes.ContainerType1_BuildingBlockType2;
import org.archifacts.core.model.ApplicationBuilderTest.Classes.ContainerType1_ExternalArtifact;
import org.archifacts.core.model.ApplicationBuilderTest.Classes.ContainerType1_MiscArtifact;
import org.archifacts.core.model.ApplicationBuilderTest.Classes.ContainerType2_BuildingBlockType1;
import org.archifacts.core.model.ApplicationBuilderTest.Classes.ContainerType2_BuildingBlockType2;
import org.archifacts.core.model.ApplicationBuilderTest.Classes.ContainerType2_ExternalArtifact;
import org.archifacts.core.model.ApplicationBuilderTest.Classes.ContainerType2_MiscArtifact;
import org.archifacts.core.model.ApplicationBuilderTest.Classes.NoContainer_BuildingBlockType1;
import org.archifacts.core.model.ApplicationBuilderTest.Classes.NoContainer_BuildingBlockType2;
import org.archifacts.core.model.ApplicationBuilderTest.Classes.NoContainer_ExternalArtifact;
import org.archifacts.core.model.ApplicationBuilderTest.Classes.NoContainer_MiscArtifact;
import org.archifacts.core.model.ApplicationBuilderTest.Descriptors.BuildingBlockType1Descriptor;
import org.archifacts.core.model.ApplicationBuilderTest.Descriptors.BuildingBlockType2Descriptor;
import org.archifacts.core.model.ApplicationBuilderTest.Descriptors.ContainerType1Descriptor;
import org.archifacts.core.model.ApplicationBuilderTest.Descriptors.ContainerType2Descriptor;
import org.archifacts.core.model.ApplicationBuilderTest.Descriptors.ExternalArtifactRelationshipDescriptor;
import org.archifacts.core.model.ApplicationBuilderTest.Descriptors.MiscArtifactRelationshipDescriptor;
import org.archifacts.core.model.ApplicationBuilderTest.Descriptors.NonMatchingBuildingBlockDescriptor;
import org.archifacts.core.model.ApplicationBuilderTest.Descriptors.SecondBuildingBlockType1Descriptor;
import org.archifacts.core.model.ApplicationBuilderTest.Descriptors.SecondContainerType1Descriptor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.tngtech.archunit.core.domain.Dependency;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaField;
import com.tngtech.archunit.core.importer.ClassFileImporter;

@DisplayNameGeneration(ReplaceUnderscores.class)
class ApplicationBuilderTest {

	private static JavaClasses javaClasses;

	@BeforeAll
	static void init() {
		javaClasses = new ClassFileImporter().importClasses(ContainerType1_BuildingBlockType1.class,
				ContainerType1_BuildingBlockType2.class, ContainerType1_MiscArtifact.class,
				ContainerType2_BuildingBlockType1.class, ContainerType2_BuildingBlockType2.class,
				ContainerType2_MiscArtifact.class, NoContainer_BuildingBlockType1.class,
				NoContainer_BuildingBlockType2.class, NoContainer_MiscArtifact.class,
				createAnonymousClass().getClass());
	}

	static final class Classes {

		static final class ContainerType1_BuildingBlockType1 {
			ContainerType1_ExternalArtifact externalArtifact;

		}

		static final class ContainerType1_BuildingBlockType2 {
			ContainerType1_MiscArtifact miscArtifact;

		}

		static final class ContainerType1_MiscArtifact {

		}

		static final class ContainerType1_ExternalArtifact {

		}

		static final class ContainerType2_BuildingBlockType1 {
			ContainerType2_ExternalArtifact externalArtifact;
		}

		static final class ContainerType2_BuildingBlockType2 {
			ContainerType2_MiscArtifact miscArtifact;
		}

		static final class ContainerType2_MiscArtifact {

		}

		static final class ContainerType2_ExternalArtifact {

		}

		static final class NoContainer_BuildingBlockType1 {
			NoContainer_ExternalArtifact externalArtifact;
		}

		static final class NoContainer_BuildingBlockType2 {
			NoContainer_MiscArtifact miscArtifact;
		}

		static final class NoContainer_MiscArtifact {

		}

		static final class NoContainer_ExternalArtifact {

		}

		static final Object createAnonymousClass() {
			return new Object() {

			};
		}
	}

	static final class Descriptors {

		static final class BuildingBlockType1Descriptor implements BuildingBlockDescriptor {

			private static final BuildingBlockType TYPE = BuildingBlockType.of("BuildingBlockType1");

			@Override
			public BuildingBlockType type() {
				return TYPE;
			}

			@Override
			public boolean isBuildingBlock(final JavaClass javaClass) {
				return javaClass.getSimpleName().endsWith("_BuildingBlockType1");
			}
		}

		static final class SecondBuildingBlockType1Descriptor implements BuildingBlockDescriptor {

			private static final BuildingBlockType TYPE = BuildingBlockType.of("BuildingBlockType1-Duplicate");

			@Override
			public BuildingBlockType type() {
				return TYPE;
			}

			@Override
			public boolean isBuildingBlock(final JavaClass javaClass) {
				return javaClass.getSimpleName().endsWith("_BuildingBlockType1");
			}
		}

		static final class BuildingBlockType2Descriptor implements BuildingBlockDescriptor {

			private static final BuildingBlockType TYPE = BuildingBlockType.of("BuildingBlockType2");

			@Override
			public BuildingBlockType type() {
				return TYPE;
			}

			@Override
			public boolean isBuildingBlock(final JavaClass javaClass) {
				return javaClass.getSimpleName().endsWith("_BuildingBlockType2");
			}
		}

		static final class ContainerType1Descriptor implements ArtifactContainerDescriptor {

			private static final ArtifactContainerType TYPE = ArtifactContainerType.of("ContainerType1");

			@Override
			public ArtifactContainerType type() {
				return TYPE;
			}

			@Override
			public Optional<String> containerNameOf(final JavaClass javaClass) {
				final String[] splittedName = javaClass.getSimpleName().split("_");
				if (splittedName.length > 1) {
					final String containerName = splittedName[0];
					if ("ContainerType1".equals(containerName)) {
						return Optional.of("Container1");
					}
				}
				return Optional.empty();
			}
		}

		static final class SecondContainerType1Descriptor implements ArtifactContainerDescriptor {

			private static final ArtifactContainerType TYPE = ArtifactContainerType.of("ContainerType1");

			@Override
			public ArtifactContainerType type() {
				return TYPE;
			}

			@Override
			public Optional<String> containerNameOf(final JavaClass javaClass) {
				final String[] splittedName = javaClass.getSimpleName().split("_");
				if (splittedName.length > 1) {
					final String containerName = splittedName[0];
					if ("ContainerType1".equals(containerName)) {
						return Optional.of("SecondContainer1");
					}
				}
				return Optional.empty();
			}
		}

		static final class ContainerType2Descriptor implements ArtifactContainerDescriptor {

			private static final ArtifactContainerType TYPE = ArtifactContainerType.of("ContainerType2");

			@Override
			public ArtifactContainerType type() {
				return TYPE;
			}

			@Override
			public Optional<String> containerNameOf(final JavaClass javaClass) {
				final String[] splittedName = javaClass.getSimpleName().split("_");
				if (splittedName.length > 1) {
					final String containerName = splittedName[0];
					if ("ContainerType2".equals(containerName)) {
						return Optional.of("Container2");
					}
				}
				return Optional.empty();
			}
		}

		static final class NonMatchingBuildingBlockDescriptor implements BuildingBlockDescriptor {

			private static final BuildingBlockType TYPE = BuildingBlockType.of("Non-matching");

			@Override
			public BuildingBlockType type() {
				return TYPE;
			}

			@Override
			public boolean isBuildingBlock(final JavaClass javaClass) {
				return false;
			}
		}

		static final class ExternalArtifactRelationshipDescriptor implements SourceBasedArtifactRelationshipDescriptor {

			private static final ArtifactRelationshipRole ROLE = ArtifactRelationshipRole.of("extref");

			@Override
			public ArtifactRelationshipRole role() {
				return ROLE;
			}

			@Override
			public boolean isSource(final Artifact sourceCandidateArtifact) {
				return sourceCandidateArtifact.getJavaClass().getFields().stream()
						.anyMatch(field -> field.getName().equals("externalArtifact"));
			}

			@Override
			public Stream<JavaClass> targets(final JavaClass sourceClass) {
				return sourceClass.getFields().stream().filter(field -> field.getName().equals("externalArtifact"))
						.map(JavaField::getRawType);
			}
		}

		static final class MiscArtifactRelationshipDescriptor implements TargetBasedArtifactRelationshipDescriptor {

			private static final ArtifactRelationshipRole ROLE = ArtifactRelationshipRole.of("miscref");

			@Override
			public ArtifactRelationshipRole role() {
				return ROLE;
			}

			@Override
			public boolean isTarget(final Artifact targetCandidateArtifact) {
				return !targetCandidateArtifact.getJavaClass().getDirectDependenciesToSelf().isEmpty();
			}

			@Override
			public Stream<JavaClass> sources(final JavaClass targetClass) {
				return targetClass.getDirectDependenciesToSelf().stream().map(Dependency::getOriginClass);
			}

		}

	}

	@Nested
	class NullParameters {

		@Test
		void assert_that_null_cannot_be_added_as_BuildingBlockDescriptor() {
			final ApplicationBuilder applicationBuilder = Application.builder();
			assertThatNullPointerException().isThrownBy(() -> applicationBuilder.addBuildingBlockDescriptor(null))
					.withMessage("The BuildingBlockDescriptor cannot be null");
		}

		@Test
		void assert_that_null_cannot_be_added_as_ContainerDescriptor() {
			final ApplicationBuilder applicationBuilder = Application.builder();
			assertThatNullPointerException().isThrownBy(() -> applicationBuilder.addContainerDescriptor(null))
					.withMessage("The ArtifactContainerDescriptor cannot be null");
		}

		@Test
		void assert_that_null_cannot_be_added_as_SourceBasedRelationshipDescriptor() {
			final ApplicationBuilder applicationBuilder = Application.builder();
			assertThatNullPointerException()
					.isThrownBy(() -> applicationBuilder.addSourceBasedRelationshipDescriptor(null))
					.withMessage("The SourceBasedArtifactRelationshipDescriptor cannot be null");
		}

		@Test
		void assert_that_null_cannot_be_added_as_TargetBasedRelationshipDescriptor() {
			final ApplicationBuilder applicationBuilder = Application.builder();
			assertThatNullPointerException()
					.isThrownBy(() -> applicationBuilder.addTargetBasedRelationshipDescriptor(null))
					.withMessage("The TargetBasedArtifactRelationshipDescriptor cannot be null");
		}
	}

	@Nested
	class ApplicationTests {

		@Nested
		@TestInstance(TestInstance.Lifecycle.PER_CLASS)
		class UnmodifiableCollections {

			Arguments argument(final String name, final Function<Application, Set<?>> method) {
				return Arguments.of(named(name, method));
			}

			Stream<Arguments> arguments() {
				return Stream.of(
						argument("getArtifacts", Application::getArtifacts),
						argument("getBuildingBlocks", Application::getBuildingBlocks),
						argument("getBuildingBlocksOfType", app -> app.getBuildingBlocksOfType(BuildingBlockType.of("TestType"))),
						argument("getContainers", Application::getContainers),
						argument("getContainersOfType", app -> app.getContainersOfType(ArtifactContainerType.of("TestType"))),
						argument("getMiscArtifacts", Application::getMiscArtifacts),
						argument("getExternalArtifacts", Application::getExternalArtifacts),
						argument("getRelationships", Application::getRelationships),
						argument("getRelationshipsOfRole", app -> app.getRelationshipsOfRole(ArtifactRelationshipRole.of("TestRole"))));
			}

			@ParameterizedTest(name = "{0}")
			@MethodSource("arguments")
			void assert_that_collections_are_unmodifiable(final Function<Application, Set<?>> setProvider) {
				final Application application = Application
						.builder()
						.buildApplication(javaClasses);
				assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> setProvider.apply(application).add(null));
			}
		}
	}

	@Nested
	class Artifacts {

		@Test
		void assert_that_matching_BuildingBlockDescriptor_creates_a_BuildingBlock() {
			final Application application = Application.builder()
					.addBuildingBlockDescriptor(new BuildingBlockType1Descriptor())
					.addBuildingBlockDescriptor(new BuildingBlockType2Descriptor())
					.addBuildingBlockDescriptor(new NonMatchingBuildingBlockDescriptor())
					.addContainerDescriptor(new ContainerType1Descriptor())
					.addContainerDescriptor(new ContainerType2Descriptor())
					.addSourceBasedRelationshipDescriptor(new ExternalArtifactRelationshipDescriptor())
					.addTargetBasedRelationshipDescriptor(new MiscArtifactRelationshipDescriptor())
					.buildApplication(javaClasses);
			assertThat(application.getBuildingBlocks())
					.extracting(BuildingBlock::getType, Artifact::getName,
							artifact -> artifact.getJavaClass().getName())
					.containsExactlyInAnyOrder(
							tuple(BuildingBlockType1Descriptor.TYPE,
									ContainerType1_BuildingBlockType1.class.getSimpleName(),
									ContainerType1_BuildingBlockType1.class.getName()),
							tuple(BuildingBlockType1Descriptor.TYPE,
									ContainerType2_BuildingBlockType1.class.getSimpleName(),
									ContainerType2_BuildingBlockType1.class.getName()),
							tuple(BuildingBlockType1Descriptor.TYPE,
									NoContainer_BuildingBlockType1.class.getSimpleName(),
									NoContainer_BuildingBlockType1.class.getName()),
							tuple(BuildingBlockType2Descriptor.TYPE,
									ContainerType1_BuildingBlockType2.class.getSimpleName(),
									ContainerType1_BuildingBlockType2.class.getName()),
							tuple(BuildingBlockType2Descriptor.TYPE,
									ContainerType2_BuildingBlockType2.class.getSimpleName(),
									ContainerType2_BuildingBlockType2.class.getName()),
							tuple(BuildingBlockType2Descriptor.TYPE,
									NoContainer_BuildingBlockType2.class.getSimpleName(),
									NoContainer_BuildingBlockType2.class.getName()));
		}

		@Test
		void assert_that_MiscArtifact_is_created_if_no_BuildingBlockDescriptor_matches() {
			final Application application = Application.builder()
					.addBuildingBlockDescriptor(new BuildingBlockType1Descriptor())
					.addBuildingBlockDescriptor(new BuildingBlockType2Descriptor())
					.addBuildingBlockDescriptor(new NonMatchingBuildingBlockDescriptor())
					.addContainerDescriptor(new ContainerType1Descriptor())
					.addContainerDescriptor(new ContainerType2Descriptor())
					.addSourceBasedRelationshipDescriptor(new ExternalArtifactRelationshipDescriptor())
					.addTargetBasedRelationshipDescriptor(new MiscArtifactRelationshipDescriptor())
					.buildApplication(javaClasses);
			assertThat(application.getMiscArtifacts())
					.extracting(Artifact::getName, artifact -> artifact.getJavaClass().getName())
					.containsExactlyInAnyOrder(
							tuple(ContainerType1_MiscArtifact.class.getSimpleName(),
									ContainerType1_MiscArtifact.class.getName()),
							tuple(ContainerType2_MiscArtifact.class.getSimpleName(),
									ContainerType2_MiscArtifact.class.getName()),
							tuple(NoContainer_MiscArtifact.class.getSimpleName(),
									NoContainer_MiscArtifact.class.getName()),
							tuple(ApplicationBuilderTest.class.getSimpleName() + "$" + Classes.class.getSimpleName()
									+ "$1", Classes.class.getName() + "$1"));
		}

		@Test
		void assert_that_ExternalArtifact_is_created() {
			final Application application = Application.builder()
					.addBuildingBlockDescriptor(new BuildingBlockType1Descriptor())
					.addBuildingBlockDescriptor(new BuildingBlockType2Descriptor())
					.addBuildingBlockDescriptor(new NonMatchingBuildingBlockDescriptor())
					.addContainerDescriptor(new ContainerType1Descriptor())
					.addContainerDescriptor(new ContainerType2Descriptor())
					.addSourceBasedRelationshipDescriptor(new ExternalArtifactRelationshipDescriptor())
					.addTargetBasedRelationshipDescriptor(new MiscArtifactRelationshipDescriptor())
					.buildApplication(javaClasses);
			assertThat(application.getExternalArtifacts())
					.extracting(Artifact::getName, artifact -> artifact.getJavaClass().getName())
					.containsExactlyInAnyOrder(
							tuple(ContainerType1_ExternalArtifact.class.getSimpleName(),
									ContainerType1_ExternalArtifact.class.getName()),
							tuple(ContainerType2_ExternalArtifact.class.getSimpleName(),
									ContainerType2_ExternalArtifact.class.getName()),
							tuple(NoContainer_ExternalArtifact.class.getSimpleName(),
									NoContainer_ExternalArtifact.class.getName()));
		}

		@Test
		void assert_that_if_multiple_BuildingBlockDescriptors_match_the_first_matching_descriptor_is_used() {
			final Application application = Application.builder()
					.addBuildingBlockDescriptor(new BuildingBlockType1Descriptor())
					.addBuildingBlockDescriptor(new SecondBuildingBlockType1Descriptor()).buildApplication(javaClasses);

			assertThat(application.getBuildingBlocks())
					.extracting(BuildingBlock::getType, Artifact::getName,
							artifact -> artifact.getJavaClass().getName())
					.containsExactlyInAnyOrder(
							tuple(BuildingBlockType1Descriptor.TYPE,
									ContainerType1_BuildingBlockType1.class.getSimpleName(),
									ContainerType1_BuildingBlockType1.class.getName()),
							tuple(BuildingBlockType1Descriptor.TYPE,
									ContainerType2_BuildingBlockType1.class.getSimpleName(),
									ContainerType2_BuildingBlockType1.class.getName()),
							tuple(BuildingBlockType1Descriptor.TYPE,
									NoContainer_BuildingBlockType1.class.getSimpleName(),
									NoContainer_BuildingBlockType1.class.getName()));
		}

		@Test
		void assert_that_building_block_types_with_same_names_are_not_considered_equal() {
			final Application application = Application
					.builder()
					.addBuildingBlockDescriptor(new BuildingBlockType1Descriptor())
					.buildApplication(javaClasses);

			assertThat(application.getBuildingBlocksOfType(BuildingBlockType1Descriptor.TYPE))
					.extracting(Artifact::getName)
					.containsExactlyInAnyOrder(
							ContainerType1_BuildingBlockType1.class.getSimpleName(),
							ContainerType2_BuildingBlockType1.class.getSimpleName(),
							NoContainer_BuildingBlockType1.class.getSimpleName());
			assertThat(application.getBuildingBlocksOfType(BuildingBlockType.of(BuildingBlockType1Descriptor.TYPE.getName()))).isEmpty();
		}

		@Nested
		@TestInstance(TestInstance.Lifecycle.PER_CLASS)
		class UnmodifiableCollections {

			Arguments argument(final String name, final Function<Artifact, Set<?>> method) {
				return Arguments.of(named(name, method));
			}

			Stream<Arguments> arguments() {
				return Stream.of(
						argument("getIncomingRelationships", Artifact::getIncomingRelationships),
						argument("getIncomingRelationshipsOfRole", artifact -> artifact.getIncomingRelationshipsOfRole(ArtifactRelationshipRole.of("TestRole"))),
						argument("getOutgoingRelationships", Artifact::getOutgoingRelationships),
						argument("getOutgoingRelationshipsOfRole", artifact -> artifact.getOutgoingRelationshipsOfRole(ArtifactRelationshipRole.of("TestRole"))));
			}

			@ParameterizedTest(name = "{0}")
			@MethodSource("arguments")
			void assert_that_collections_are_unmodifiable(final Function<Artifact, Set<?>> setProvider) {
				final Application application = Application
						.builder()
						.addBuildingBlockDescriptor(new BuildingBlockType1Descriptor())
						.buildApplication(javaClasses);
				final Artifact artifact = application.getArtifacts().iterator().next();
				assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> setProvider.apply(artifact).add(null));
			}
		}
	}

	@Nested
	class Containers {

		@Test
		void assert_that_BuildingBlock_can_have_a_container() {
			final Application application = Application.builder()
					.addBuildingBlockDescriptor(new BuildingBlockType1Descriptor())
					.addBuildingBlockDescriptor(new BuildingBlockType2Descriptor())
					.addBuildingBlockDescriptor(new NonMatchingBuildingBlockDescriptor())
					.addContainerDescriptor(new ContainerType1Descriptor())
					.addContainerDescriptor(new ContainerType2Descriptor())
					.addSourceBasedRelationshipDescriptor(new ExternalArtifactRelationshipDescriptor())
					.addTargetBasedRelationshipDescriptor(new MiscArtifactRelationshipDescriptor())
					.buildApplication(javaClasses);
			final Artifact testEventArtifact = application.getArtifacts().stream().filter(artifact -> artifact
					.getJavaClass().getName().equals(ContainerType1_BuildingBlockType1.class.getName())).findFirst()
					.orElseThrow();
			assertThat(testEventArtifact.getContainer()).isPresent().get()
					.extracting(ArtifactContainer::getName, ArtifactContainer::getType)
					.containsExactly("Container1", ContainerType1Descriptor.TYPE);
		}

		@Test
		void assert_that_MiscArtifact_can_have_a_container() {
			final Application application = Application.builder()
					.addBuildingBlockDescriptor(new BuildingBlockType1Descriptor())
					.addBuildingBlockDescriptor(new BuildingBlockType2Descriptor())
					.addBuildingBlockDescriptor(new NonMatchingBuildingBlockDescriptor())
					.addContainerDescriptor(new ContainerType1Descriptor())
					.addContainerDescriptor(new ContainerType2Descriptor())
					.addSourceBasedRelationshipDescriptor(new ExternalArtifactRelationshipDescriptor())
					.addTargetBasedRelationshipDescriptor(new MiscArtifactRelationshipDescriptor())
					.buildApplication(javaClasses);
			final Artifact anyClassArtifact = application.getArtifacts().stream().filter(
					artifact -> artifact.getJavaClass().getName().equals(ContainerType1_MiscArtifact.class.getName()))
					.findFirst().orElseThrow();
			assertThat(anyClassArtifact.getContainer()).isPresent().get()
					.extracting(ArtifactContainer::getName, ArtifactContainer::getType)
					.containsExactly("Container1", ContainerType1Descriptor.TYPE);
		}

		@Test
		void assert_that_BuildingBlock_can_have_no_container() {
			final Application application = Application.builder()
					.addBuildingBlockDescriptor(new BuildingBlockType1Descriptor())
					.addBuildingBlockDescriptor(new BuildingBlockType2Descriptor())
					.addBuildingBlockDescriptor(new NonMatchingBuildingBlockDescriptor())
					.addContainerDescriptor(new ContainerType1Descriptor())
					.addContainerDescriptor(new ContainerType2Descriptor())
					.addSourceBasedRelationshipDescriptor(new ExternalArtifactRelationshipDescriptor())
					.addTargetBasedRelationshipDescriptor(new MiscArtifactRelationshipDescriptor())
					.buildApplication(javaClasses);
			final Artifact testEventArtifact = application.getArtifacts().stream().filter(artifact -> artifact
					.getJavaClass().getName().equals(NoContainer_BuildingBlockType1.class.getName())).findFirst()
					.orElseThrow();
			assertThat(testEventArtifact.getContainer()).isEmpty();
		}

		@Test
		void assert_that_MiscArtifact_can_have_no_container() {
			final Application application = Application.builder()
					.addBuildingBlockDescriptor(new BuildingBlockType1Descriptor())
					.addBuildingBlockDescriptor(new BuildingBlockType2Descriptor())
					.addBuildingBlockDescriptor(new NonMatchingBuildingBlockDescriptor())
					.addContainerDescriptor(new ContainerType1Descriptor())
					.addContainerDescriptor(new ContainerType2Descriptor())
					.addSourceBasedRelationshipDescriptor(new ExternalArtifactRelationshipDescriptor())
					.addTargetBasedRelationshipDescriptor(new MiscArtifactRelationshipDescriptor())
					.buildApplication(javaClasses);
			final Artifact testEventArtifact = application.getArtifacts().stream().filter(
					artifact -> artifact.getJavaClass().getName().equals(NoContainer_MiscArtifact.class.getName()))
					.findFirst().orElseThrow();
			assertThat(testEventArtifact.getContainer()).isEmpty();
		}

		@Test
		void assert_that_expected_containers_are_present() {
			final Application application = Application.builder()
					.addBuildingBlockDescriptor(new BuildingBlockType1Descriptor())
					.addBuildingBlockDescriptor(new BuildingBlockType2Descriptor())
					.addBuildingBlockDescriptor(new NonMatchingBuildingBlockDescriptor())
					.addContainerDescriptor(new ContainerType1Descriptor())
					.addContainerDescriptor(new ContainerType2Descriptor())
					.addSourceBasedRelationshipDescriptor(new ExternalArtifactRelationshipDescriptor())
					.addTargetBasedRelationshipDescriptor(new MiscArtifactRelationshipDescriptor())
					.buildApplication(javaClasses);

			assertThat(application.getContainers()).extracting(ArtifactContainer::getName, ArtifactContainer::getType)
					.containsExactlyInAnyOrder(tuple("Container1", ContainerType1Descriptor.TYPE),
							tuple("Container2", ContainerType2Descriptor.TYPE));
		}

		@Test
		void assert_that_if_multiple_ContainerDescriptors_match_the_first_matching_descriptor_is_used() {
			final Application application = Application
					.builder()
					.addContainerDescriptor(new ContainerType1Descriptor())
					.addContainerDescriptor(new SecondContainerType1Descriptor())
					.buildApplication(javaClasses);

			assertThat(application.getContainers()).extracting(ArtifactContainer::getName, ArtifactContainer::getType)
					.containsExactlyInAnyOrder(tuple("Container1", ContainerType1Descriptor.TYPE));
		}

		@Test
		void assert_that_container_types_with_same_names_are_not_considered_equal() {
			final Application application = Application
					.builder()
					.addContainerDescriptor(new ContainerType1Descriptor())
					.buildApplication(javaClasses);

			assertThat(application.getContainersOfType(ContainerType1Descriptor.TYPE))
					.extracting(ArtifactContainer::getName)
					.containsExactlyInAnyOrder("Container1");
			assertThat(application.getContainersOfType(ArtifactContainerType.of(ContainerType1Descriptor.TYPE.getName()))).isEmpty();
		}

		@Nested
		@TestInstance(TestInstance.Lifecycle.PER_CLASS)
		class UnmodifiableCollections {

			Arguments argument(final String name, final Function<ArtifactContainer, Set<?>> method) {
				return Arguments.of(named(name, method));
			}

			Stream<Arguments> arguments() {
				return Stream.of(
						argument("getArtifacts", ArtifactContainer::getArtifacts),
						argument("getBuildingBlocks", ArtifactContainer::getBuildingBlocks),
						argument("getBuildingBlocksOfType", container -> container.getBuildingBlocksOfType(BuildingBlockType.of("TestType"))),
						argument("getMiscArtifacts", ArtifactContainer::getMiscArtifacts),
						argument("getExternalArtifacts", ArtifactContainer::getExternalArtifacts),
						argument("getIncomingRelationships", ArtifactContainer::getIncomingRelationships),
						argument("getIncomingRelationshipsOfRole", container -> container.getIncomingRelationshipsOfRole(ArtifactRelationshipRole.of("TestRole"))),
						argument("getOutgoingRelationships", ArtifactContainer::getOutgoingRelationships),
						argument("getOutgoingRelationshipsOfRole", container -> container.getOutgoingRelationshipsOfRole(ArtifactRelationshipRole.of("TestRole"))));
			}

			@ParameterizedTest(name = "{0}")
			@MethodSource("arguments")
			void assert_that_collections_are_unmodifiable(final Function<ArtifactContainer, Set<?>> setProvider) {
				final Application application = Application
						.builder()
						.addContainerDescriptor(new ContainerType1Descriptor())
						.buildApplication(javaClasses);
				final ArtifactContainer container = application.getContainers().iterator().next();
				assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> setProvider.apply(container).add(null));
			}
		}
	}

	@Nested
	class Relationships {
		@Test
		void assert_that_relationships_are_created() {
			final Application application = Application.builder()
					.addBuildingBlockDescriptor(new BuildingBlockType1Descriptor())
					.addBuildingBlockDescriptor(new BuildingBlockType2Descriptor())
					.addBuildingBlockDescriptor(new NonMatchingBuildingBlockDescriptor())
					.addContainerDescriptor(new ContainerType1Descriptor())
					.addContainerDescriptor(new ContainerType2Descriptor())
					.addSourceBasedRelationshipDescriptor(new ExternalArtifactRelationshipDescriptor())
					.addTargetBasedRelationshipDescriptor(new MiscArtifactRelationshipDescriptor())
					.buildApplication(javaClasses);
			assertThat(application.getRelationships())
					.extracting(ArtifactRelationship::getRole, rel -> rel.getSource().getName(),
							rel -> rel.getTarget().getName())
					.containsExactlyInAnyOrder(
							tuple(ExternalArtifactRelationshipDescriptor.ROLE,
									ContainerType1_BuildingBlockType1.class.getSimpleName(),
									ContainerType1_ExternalArtifact.class.getSimpleName()),
							tuple(ExternalArtifactRelationshipDescriptor.ROLE,
									ContainerType2_BuildingBlockType1.class.getSimpleName(),
									ContainerType2_ExternalArtifact.class.getSimpleName()),
							tuple(ExternalArtifactRelationshipDescriptor.ROLE,
									NoContainer_BuildingBlockType1.class.getSimpleName(),
									NoContainer_ExternalArtifact.class.getSimpleName()),
							tuple(MiscArtifactRelationshipDescriptor.ROLE,
									ContainerType1_BuildingBlockType2.class.getSimpleName(),
									ContainerType1_MiscArtifact.class.getSimpleName()),
							tuple(MiscArtifactRelationshipDescriptor.ROLE,
									ContainerType2_BuildingBlockType2.class.getSimpleName(),
									ContainerType2_MiscArtifact.class.getSimpleName()),
							tuple(MiscArtifactRelationshipDescriptor.ROLE,
									NoContainer_BuildingBlockType2.class.getSimpleName(),
									NoContainer_MiscArtifact.class.getSimpleName()));
		}
	}

	@Nested
	class BidirectionalModelTest {
		@Test
		void assert_artifacts_in_container() {
			final Application application = Application.builder()
					.addBuildingBlockDescriptor(new BuildingBlockType1Descriptor())
					.addBuildingBlockDescriptor(new BuildingBlockType2Descriptor())
					.addBuildingBlockDescriptor(new NonMatchingBuildingBlockDescriptor())
					.addContainerDescriptor(new ContainerType1Descriptor())
					.addContainerDescriptor(new ContainerType2Descriptor())
					.addSourceBasedRelationshipDescriptor(new ExternalArtifactRelationshipDescriptor())
					.addTargetBasedRelationshipDescriptor(new MiscArtifactRelationshipDescriptor())
					.buildApplication(javaClasses);

			application.getContainers().forEach(container -> {
				final Set<Artifact> artifactsWithThisContainer = application.getArtifacts().stream()
						.filter(artifact -> artifact.getContainer().map(container::equals).orElse(false))
						.collect(Collectors.toSet());
				assertThat(container.getArtifacts()).containsExactlyInAnyOrderElementsOf(artifactsWithThisContainer);
			});

			application.getArtifacts().stream().forEach(artifact -> {
				final Set<ArtifactContainer> containersWithThisArtifact = application.getContainers().stream()
						.filter(container -> container.getArtifacts().contains(artifact)).collect(Collectors.toSet());
				if (artifact.getContainer().isPresent()) {
					assertThat(containersWithThisArtifact).hasSize(1);
					assertThat(artifact.getContainer()).contains(containersWithThisArtifact.iterator().next());
				} else {
					assertThat(containersWithThisArtifact).isEmpty();
				}
			});
		}

		@Test
		void assert_relationships() {
			final Application application = Application.builder()
					.addBuildingBlockDescriptor(new BuildingBlockType1Descriptor())
					.addBuildingBlockDescriptor(new BuildingBlockType2Descriptor())
					.addBuildingBlockDescriptor(new NonMatchingBuildingBlockDescriptor())
					.addContainerDescriptor(new ContainerType1Descriptor())
					.addContainerDescriptor(new ContainerType2Descriptor())
					.addSourceBasedRelationshipDescriptor(new ExternalArtifactRelationshipDescriptor())
					.addTargetBasedRelationshipDescriptor(new MiscArtifactRelationshipDescriptor())
					.buildApplication(javaClasses);

			application.getRelationships().forEach(relationship -> {
				final Artifact source = relationship.getSource();
				final Artifact target = relationship.getTarget();
				assertThat(source.getOutgoingRelationships()).contains(relationship);
				assertThat(target.getIncomingRelationships()).contains(relationship);
			});

			final Set<ArtifactRelationship> allRelationships = application.getArtifacts().stream()
					.flatMap(artifact -> Stream.concat(artifact.getIncomingRelationships().stream(),
							artifact.getOutgoingRelationships().stream()))
					.collect(Collectors.toSet());
			assertThat(application.getRelationships()).containsExactlyInAnyOrderElementsOf(allRelationships);

		}

		@Test
		void assert_that_roles_with_same_names_are_not_considered_equal() {
			final Application application = Application
					.builder()
					.addSourceBasedRelationshipDescriptor(new ExternalArtifactRelationshipDescriptor())
					.buildApplication(javaClasses);

			assertThat(application.getRelationshipsOfRole(ExternalArtifactRelationshipDescriptor.ROLE))
					.extracting(a -> a.getSource().getName())
					.containsExactlyInAnyOrder(
							"NoContainer_BuildingBlockType1",
							"ContainerType2_BuildingBlockType1",
							"ContainerType1_BuildingBlockType1");
			assertThat(application.getRelationshipsOfRole(ArtifactRelationshipRole.of(ExternalArtifactRelationshipDescriptor.ROLE.getName()))).isEmpty();
		}

		@Test
		void assert_that_outgoing_relationships_of_building_blocks_can_be_queried() {
			final BuildingBlockDescriptor blockDescriptor = BuildingBlockDescriptor.forAssignableTo(BuildingBlockType.of("test"), ContainerType1_BuildingBlockType1.class);
			final ExternalArtifactRelationshipDescriptor relationshipDescriptor = new ExternalArtifactRelationshipDescriptor();

			final Application application = Application
					.builder()
					.addBuildingBlockDescriptor(blockDescriptor)
					.addSourceBasedRelationshipDescriptor(relationshipDescriptor)
					.buildApplication(javaClasses);

			final BuildingBlock buildingBlock = application.getBuildingBlocksOfType(blockDescriptor.type()).iterator().next();
			assertThat(buildingBlock.getOutgoingRelationshipsOfRole(relationshipDescriptor.role()))
					.extracting(r -> r.getRole())
					.containsExactly(relationshipDescriptor.role());
			assertThat(buildingBlock.getOutgoingRelationshipsOfRole(ArtifactRelationshipRole.of("non-existing")))
					.isEmpty();
		}

		@Test
		void assert_that_incoming_relationships_of_building_blocks_can_be_queried() {
			final BuildingBlockDescriptor blockDescriptor = BuildingBlockDescriptor.forAssignableTo(BuildingBlockType.of("test"), NoContainer_MiscArtifact.class);
			final MiscArtifactRelationshipDescriptor relationshipDescriptor = new MiscArtifactRelationshipDescriptor();

			final Application application = Application
					.builder()
					.addBuildingBlockDescriptor(blockDescriptor)
					.addTargetBasedRelationshipDescriptor(relationshipDescriptor)
					.buildApplication(javaClasses);

			final BuildingBlock buildingBlock = application.getBuildingBlocksOfType(blockDescriptor.type()).iterator().next();
			assertThat(buildingBlock.getIncomingRelationshipsOfRole(relationshipDescriptor.role()))
					.extracting(r -> r.getRole())
					.containsExactly(relationshipDescriptor.role());
			assertThat(buildingBlock.getIncomingRelationshipsOfRole(ArtifactRelationshipRole.of("non-existing")))
					.isEmpty();
		}

		@Test
		void assert_that_outgoing_relationships_of_artifact_containers_can_be_queried() {
			final ContainerType1Descriptor containerDescriptor = new ContainerType1Descriptor();
			final ExternalArtifactRelationshipDescriptor relationshipDescriptor = new ExternalArtifactRelationshipDescriptor();

			final Application application = Application
					.builder()
					.addContainerDescriptor(containerDescriptor)
					.addSourceBasedRelationshipDescriptor(relationshipDescriptor)
					.buildApplication(javaClasses);

			final ArtifactContainer artifactContainera = application.getContainersOfType(containerDescriptor.type()).iterator().next();
			assertThat(artifactContainera.getOutgoingRelationshipsOfRole(relationshipDescriptor.role()))
					.extracting(r -> r.getRole())
					.containsExactly(relationshipDescriptor.role());
			assertThat(artifactContainera.getOutgoingRelationshipsOfRole(ArtifactRelationshipRole.of("non-existing")))
					.isEmpty();
		}

		@Test
		void assert_that_incoming_relationships_of_artifact_containers_can_be_queried() {
			final ContainerType1Descriptor containerDescriptor = new ContainerType1Descriptor();
			final MiscArtifactRelationshipDescriptor relationshipDescriptor = new MiscArtifactRelationshipDescriptor();

			final Application application = Application
					.builder()
					.addContainerDescriptor(containerDescriptor)
					.addTargetBasedRelationshipDescriptor(relationshipDescriptor)
					.buildApplication(javaClasses);

			final ArtifactContainer artifactContainera = application.getContainersOfType(containerDescriptor.type()).iterator().next();
			assertThat(artifactContainera.getIncomingRelationshipsOfRole(relationshipDescriptor.role()))
					.extracting(r -> r.getRole())
					.containsExactly(relationshipDescriptor.role());
			assertThat(artifactContainera.getIncomingRelationshipsOfRole(ArtifactRelationshipRole.of("non-existing")))
					.isEmpty();
		}

	}

}
