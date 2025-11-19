// Copyright Epic Games, Inc. All Rights Reserved.

using UnrealBuildTool;

public class LyraEditor : ModuleRules
{
    public LyraEditor(ReadOnlyTargetRules Target) : base(Target)
    {
        PCHUsage = PCHUsageMode.UseExplicitOrSharedPCHs;

		PublicIncludePaths.AddRange(
			new string[] {
				"LyraEditor"
			}
		);

		PrivateIncludePaths.AddRange(
			new string[] {
			}
		);

		PublicDependencyModuleNames.AddRange(
            new string[] {
                "Core",
                "CoreUObject",
                "Engine",
                "EditorFramework",
                "UnrealEd",
				"PhysicsCore",
				"GameplayTagsEditor",
				"GameplayTasksEditor",
				"GameplayAbilities",
				"GameplayAbilitiesEditor",
				"StudioTelemetry",
				"LyraGame",
            }
        );

        PrivateDependencyModuleNames.AddRange(
            new string[] {
				"InputCore",
				"Slate",
				"SlateCore",
				"ToolMenus",
				"EditorStyle",
				"DataValidation",
				"MessageLog",
				"Projects",
				"DeveloperToolSettings",
				"CollectionManager",
				"SourceControl",
				"Chaos"
			}
        );

		DynamicallyLoadedModuleNames.AddRange(
			new string[] {
			}
		);
		// Basic setup for External RPC Framework.
		// Functionality within framework will be stripped in shipping to remove vulnerabilities.
		// Still disabled in UE 5.7 - GetObjectW API issue persists
		//PrivateDependencyModuleNames.Add("ExternalRpcRegistry");
		//PrivateDependencyModuleNames.Add("HTTPServer");
		PublicDefinitions.Add("WITH_RPC_REGISTRY=0");
		PublicDefinitions.Add("WITH_HTTPSERVER_LISTENERS=0");
		// Generate compile errors if using DrawDebug functions in test/shipping builds.
		PublicDefinitions.Add("SHIPPING_DRAW_DEBUG_ERROR=1");
		
		// UE 5.7: Set UndefinedIdentifierWarningLevel to match UnrealEditor
		UndefinedIdentifierWarningLevel = WarningLevel.Error;
    }
}
